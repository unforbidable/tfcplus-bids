package com.unforbidable.tfc.bids.features.device.processingsurface.tileentity;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.device.processingsurface.main.ProcessingSurfaceHelper;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityProcessingSurface extends TileEntity implements PacketHandler<SimpleUpdatePacket> {

    // Copper material is used for the base efficiency
    private static final float BASE_TOOL_EFFICIENCY = 8.0f;

    // How much work one block activation does
    // Stone knife is used as the baseline, which should match the TFC Leather Rack speed
    // Better material will do more work with each block activation
    private static final float WORK_AMOUNT_PER_TOOL_EFFICIENCY = 1f / BASE_TOOL_EFFICIENCY;

    // Default max work is multiplied by recipe difficulty
    // This allows scrapping larger hides to take more time and more tool durability
    // Number 16 corresponds to the default TFC scrapping work grid of 4x4
    private static final float DEFAULT_MAX_WORK = 16;

    private final ItemStack[] tickingStorage = new ItemStack[1];

    private ItemStack inputItem = null;
    private ItemStack resultItem = null;

    private float workCounter = 0;

    boolean clientNeedToUpdate = true;

    private ProcessingSurfaceRecipe cachedRecipe = null;

    private final Random toolDamageFractionRandom = new Random();

    public ItemStack getInputItem() {
        return inputItem;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    private ProcessingSurfaceRecipe getCurrentRecipe() {
        if (cachedRecipe == null) {
            cachedRecipe = ProcessingSurfaceHelper.findMatchingRecipe(inputItem, worldObj, xCoord, yCoord - 1, zCoord);
        }

        return cachedRecipe;
    }

    public void setInputItem(ItemStack inputItem) {
        this.inputItem = inputItem;

        if (inputItem != null) {
            ProcessingSurfaceRecipe recipe = getCurrentRecipe();
            if (recipe != null) {
                resultItem = recipe.getResult(inputItem);
            }
        }
    }

    public void workItem(EntityPlayer player) {
        if (!isWorkDone()) {
            ProcessingSurfaceRecipe recipe = getCurrentRecipe();
            if (recipe != null && recipe.matchesTool(player.getHeldItem())) {
                // Effort from recipe
                float originalEffort = recipe.getEffort(inputItem);
                // and it can be further modified in event
                float newEffort = BidsEventFactory.onProcessingSurfaceEffortCheck(this, inputItem, resultItem, player.getHeldItem(), player, originalEffort);
                // Efficiency is based on tool material
                float originalEfficiency = ProcessingSurfaceHelper.getToolEfficiency(player.getHeldItem());
                // and it can be further modified in event
                float newEfficiency = BidsEventFactory.onProcessingSurfaceToolEfficiencyCheck(this, inputItem, resultItem, player.getHeldItem(), player, originalEfficiency);
                if (newEfficiency > 0) {
                    float workAmount = newEfficiency * WORK_AMOUNT_PER_TOOL_EFFICIENCY;

                    float prevWorkCounter = workCounter;
                    workCounter = Math.min(workCounter + (workAmount / newEffort), DEFAULT_MAX_WORK);

                    float progress = getWorkProgress();

                    if (!worldObj.isRemote) {
                        BidsEventFactory.onProcessingSurfaceProgress(this, inputItem, resultItem, player.getHeldItem(), player, progress, newEffort);

                        if (progress == 1f) {
                            if (recipe.isPartial()) {
                                // Partial recipes process only part of the item stack per work cycle
                                float recipeInputWeight = Food.getWeight(recipe.getInput());
                                float recipeOutputWeight = Food.getWeight(recipe.getOutput());
                                float inOutRatio = recipeOutputWeight / recipeInputWeight;

                                float inputWeight = Food.getWeight(inputItem);
                                float inputDecay = Math.max(0, Food.getDecay(inputItem));
                                float weightIn = Math.min(recipeInputWeight, inputWeight - inputDecay);
                                float wightOut = weightIn * inOutRatio;

                                ItemStack inputItemPartial = inputItem.copy();
                                Food.setWeight(inputItemPartial, weightIn);
                                Food.setDecay(inputItemPartial, 0);

                                ItemStack resultItemPartial = resultItem.copy();
                                resultItemPartial.stackSize = 1;

                                Food.setWeight(resultItemPartial, wightOut);
                                Food.setDecay(resultItemPartial, 0);
                                Food.setDecayTimer(resultItemPartial, (int) TFC_Time.getTotalHours() + 1);

                                BidsEventFactory.onProcessingItemCrafted(inputItemPartial, resultItemPartial, player);

                                // Partial result is dropped and input is subtracted
                                EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, resultItemPartial);
                                ei.motionX = 0;
                                ei.motionZ = 0;
                                worldObj.spawnEntityInWorld(ei);

                                Food.setWeight(inputItem, inputWeight - Food.getWeight(inputItemPartial));
                            } else {
                                BidsEventFactory.onProcessingItemCrafted(inputItem, resultItem, player);
                            }
                        }

                        if ((int) Math.floor(workCounter) > (int) Math.floor(prevWorkCounter)) {
                            // tool damage can be less than one if effort is a fraction
                            if (newEffort < 1) {
                                // fractions should add up at random over time
                                if (toolDamageFractionRandom.nextFloat() < newEffort) {
                                    player.getHeldItem().damageItem(1, player);
                                }
                            } else {
                                player.getHeldItem().damageItem(1, player);
                            }
                        }
                    }

                    if (worldObj.isRemote) {
                        int visualProgress = (int) Math.floor(workCounter) - (int) Math.floor(prevWorkCounter);
                        if (visualProgress > 0) {
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }
                    }

                    if (progress == 1f && recipe.isPartial()) {
                        // Reset progress for partial recipes
                        workCounter = 0;
                    }
                }
            }
        }
    }

    public int getWorkCounter() {
        return (int) Math.floor(workCounter);
    }

    public float getWorkProgress() {
        if (isWorkDone()) {
            return 1f;
        } else {
            return workCounter / DEFAULT_MAX_WORK;
        }
    }

    public boolean isWorkDone() {
        return workCounter >= DEFAULT_MAX_WORK;
    }

    public void onProcessingSurfaceBroken() {
        ItemStack is = getCurrentItem();
        if (is != null) {
            EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, is);
            ei.motionX = 0;
            ei.motionZ = 0;
            worldObj.spawnEntityInWorld(ei);
        }
    }

    public ItemStack getCurrentItem() {
        if (isWorkDone()) {
            return getResultItem();
        } else {
            return getInputItem();
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (clientNeedToUpdate) {
                sendUpdateMessage();

                clientNeedToUpdate = false;
            }

            handleItemsTicking();
        }
    }

    private void handleItemsTicking() {
        tickingStorage[0] = inputItem;
        TFC_Core.handleItemTicking(tickingStorage, worldObj, xCoord, yCoord, zCoord, 1, 1, false);
        inputItem = tickingStorage[0];

        if (inputItem == null) {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeTileEntityDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readTileEntityDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeTileEntityDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readTileEntityDataFromNBT(tag);
    }

    public void writeTileEntityDataToNBT(NBTTagCompound tag) {
        tag.setFloat("workCounter", workCounter);

        if (inputItem != null) {
            NBTTagCompound itemTag = new NBTTagCompound();
            inputItem.writeToNBT(itemTag);
            tag.setTag("inputItem", itemTag);
        }

        if (resultItem != null) {
            NBTTagCompound resultTag = new NBTTagCompound();
            resultItem.writeToNBT(resultTag);
            tag.setTag("resultItem", resultTag);
        }
    }

    public void readTileEntityDataFromNBT(NBTTagCompound tag) {
        workCounter = tag.getFloat("workCounter");

        NBTTagCompound itemTag = tag.getCompoundTag("inputItem");
        inputItem = ItemStack.loadItemStackFromNBT(itemTag);

        NBTTagCompound resultTag = tag.getCompoundTag("resultItem");
        resultItem = ItemStack.loadItemStackFromNBT(resultTag);
    }

    public void sendUpdateMessage() {
        Network.sendToTileEntity(new SimpleUpdatePacket(), this);
        Bids.LOG.debug("Sent update message");
    }

    @Override
    public void handleNetworkPacket(SimpleUpdatePacket packet) {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Bids.LOG.debug("Client updated at [{},{},{}]", xCoord, yCoord, zCoord);
    }

}
