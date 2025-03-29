package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.ProcessingSurface.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityProcessingSurface extends TileEntity {

    // How much work one block activation does
    // Stone knife is used as the baseline, which should match the TFC Leather Rack speed
    // Better material will do more work with each block activation
    private static final float WORK_AMOUNT_PER_TOOL_EFFICIENCY = 1f / TFCItems.igExToolMaterial.getEfficiencyOnProperMaterial();

    // Default max work is multiplied by recipe difficulty
    // This allows scrapping larger hides to take more time and more tool durability
    // Number 16 corresponds to the default TFC scrapping work grid of 4x4
    private static final float DEFAULT_MAX_WORK = 16;

    private ItemStack inputItem = null;
    private ItemStack resultItem = null;

    private float workCounter = 0;

    private ProcessingSurfaceRecipe cachedRecipe = null;

    public ItemStack getInputItem() {
        return inputItem;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    private ProcessingSurfaceRecipe getCurrentRecipe() {
        if (cachedRecipe == null) {
            cachedRecipe = ProcessingSurfaceManager.findMatchingRecipe(inputItem, worldObj, xCoord, yCoord - 1, zCoord);
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
                float workAmount = ProcessingSurfaceHelper.getToolEfficiency(player.getHeldItem()) * WORK_AMOUNT_PER_TOOL_EFFICIENCY;

                float prevWorkCounter = workCounter;
                workCounter += workAmount;

                int toolDamage = (int) Math.floor(workCounter) - (int) Math.floor(prevWorkCounter);
                if (toolDamage > 0) {
                    player.getHeldItem().damageItem(toolDamage, player);
                }

                if (worldObj.isRemote) {
                    int visualProgress = (int) Math.floor(workCounter / recipe.getEffort()) - (int) Math.floor(prevWorkCounter / recipe.getEffort());
                    if (visualProgress > 0) {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            }
        }
    }

    public int getWorkCounter() {
        ProcessingSurfaceRecipe recipe = getCurrentRecipe();
        if (recipe != null) {
            return (int) Math.floor(workCounter / recipe.getEffort());
        } else {
            return (int) Math.floor(workCounter);
        }
    }

    public float getWorkProgress() {
        if (isWorkDone()) {
            return 1f;
        } else {
            return workCounter / getMaxWork();
        }
    }

    public boolean isWorkDone() {
        return workCounter >= getMaxWork();
    }

    private float getMaxWork() {
        ProcessingSurfaceRecipe recipe = getCurrentRecipe();
        return recipe != null ? recipe.getEffort() * DEFAULT_MAX_WORK : DEFAULT_MAX_WORK;
    }

    public void onProcessingSurfaceBroken() {
        ItemStack is = getCurrentItem();
        if (is != null) {
            EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
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

        NBTTagCompound itemTag = new NBTTagCompound();
        inputItem.writeToNBT(itemTag);
        tag.setTag("inputItem", itemTag);

        NBTTagCompound resultTag = new NBTTagCompound();
        resultItem.writeToNBT(resultTag);
        tag.setTag("resultItem", resultTag);
    }

    public void readTileEntityDataFromNBT(NBTTagCompound tag) {
        workCounter = tag.getFloat("workCounter");

        NBTTagCompound itemTag = tag.getCompoundTag("inputItem");
        inputItem = ItemStack.loadItemStackFromNBT(itemTag);

        NBTTagCompound resultTag = tag.getCompoundTag("resultItem");
        resultItem = ItemStack.loadItemStackFromNBT(resultTag);
    }

}
