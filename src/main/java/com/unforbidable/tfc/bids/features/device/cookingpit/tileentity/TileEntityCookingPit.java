package com.unforbidable.tfc.bids.features.device.cookingpit.tileentity;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingAccessory;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHost;
import com.unforbidable.tfc.bids.api.features.cooking.CookingLidUsage;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.api.meta.HeatingStoneMeta;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingEngine;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTicker;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTickerResult;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingRecipeProgress;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitBounds;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.material.heatingstone.item.ItemHeatingStone;
import com.unforbidable.tfc.bids.util.EntityScanner;
import com.unforbidable.tfc.bids.util.Timer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public abstract class TileEntityCookingPit extends TileEntity implements CookingHost, PacketHandler<SimpleUpdatePacket>, IInventory {

    private static final int MAX_STORAGE = 2;
    private static final int MAX_FLUIDS = 1;

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_STONES = 1;

    private static final int FLUID_PRIMARY = 0;

    private static final int HEAT_CHECK_INTERVAL = 10;
    private static final int ITEM_TICK_INTERVAL = 50;

    private final ItemStack[] storage = new ItemStack[MAX_STORAGE];
    private final FluidStack[] fluids = new FluidStack[MAX_FLUIDS];

    private CookingHeatLevel heatLevel = CookingHeatLevel.NONE;
    private long heatedUntilTicks = 0;

    boolean clientNeedToUpdate = false;
    boolean clientDataLoaded = false;
    boolean inputItemSelected = false;

    private final CookingEngine cookingEngine = new CookingEngine(this);
    private final CookingFoodTicker cookingFoodTicker = new CookingFoodTicker(this, ITEM_TICK_INTERVAL);

    private final Timer heatCheckTimer = new Timer(HEAT_CHECK_INTERVAL);
    private final Timer itemTickTimer = new Timer(ITEM_TICK_INTERVAL);

    public boolean isClientDataLoaded() {
        return clientDataLoaded;
    }

    public CookingPitBounds getCachedBounds() {
        return CookingPitBounds.getBounds();
    }

    public void setInputItemSelected(boolean selected) {
        inputItemSelected = selected;
    }

    public boolean isInputItemSelected() {
        return inputItemSelected;
    }

    @Override
    public CookingHeatLevel getHeatLevel() {
        return heatLevel;
    }

    public long getHeatedUntilTicks() {
        return heatedUntilTicks;
    }

    public boolean hasInputItem() {
        return storage[SLOT_INPUT] != null;
    }

    @Override
    public ItemStack getInputItemStack() {
        return storage[SLOT_INPUT];
    }

    public int getTotalLiquidVolume() {
        return hasFluid() ? getFluidStack().amount : 0;
    }

    public abstract int getMaxFluidVolume();

    public boolean hasFluid() {
        return fluids[FLUID_PRIMARY] != null;
    }

    @Override
    public FluidStack getFluidStack() {
        return fluids[FLUID_PRIMARY];
    }

    public void onBreakBlock() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                    storage[i]);
                worldObj.spawnEntityInWorld(ei);

                storage[i] = null;
            }
        }
    }

    @Override
    public CookingRecipeInputTemplate createRecipeTemplate() {
        return new CookingRecipeInputTemplate(
            getFluidStack(), null,
            getInputItemStack(),
            CookingAccessory.NONE,
            CookingLidUsage.OFF,
            getHeatLevel(), getHeatLevel());
    }

    @Override
    public boolean isValidCookingRecipe(CookingRecipe recipe) {
        return true;
    }

    public CookingRecipeProgress getRecipeProgress() {
        return cookingEngine.getRecipeProgress();
    }

    @Override
    public void onItemChanged(ItemStack output) {
        storage[SLOT_INPUT] = output;

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();

        clientNeedToUpdate = true;
    }

    @Override
    public void onFluidChanged(FluidStack output, FluidStack secondaryOutput) {
        fluids[FLUID_PRIMARY] = output;

        if (output == null) {
            heatLevel = CookingHeatLevel.NONE;
            onHeatLevelChanged();
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();

        clientNeedToUpdate = true;
    }

    public void retrieveItemStack(EntityPlayer player) {
        if (hasInputItem()) {
            Bids.LOG.debug("Retrieving input item: " + storage[SLOT_INPUT].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_INPUT]);
            storage[SLOT_INPUT] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(true);
        }
    }

    public void retrieveStones(EntityPlayer player) {
        if (heatLevel == CookingHeatLevel.NONE && storage[SLOT_STONES] != null) {
            giveItemStackToPlayer(player, storage[SLOT_STONES]);
            storage[SLOT_STONES] = null;
        }
    }

    public boolean interactWithItemStack(ItemStack equippedItem, EntityPlayer player) {
        return cookingEngine.handleItemInteraction(equippedItem, player);
    }

    private void giveItemStackToPlayer(EntityPlayer player, ItemStack itemStack) {
        final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, itemStack);
        worldObj.spawnEntityInWorld(ei);
    }

    @Override
    public abstract boolean isValidFluidStack(FluidStack fluid);

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage();

                clientNeedToUpdate = false;
            }

            if (heatCheckTimer.tick()) {
                findAndConsumeHeatingStones();
                checkRemainingHeat();
            }

            if (cookingEngine.update()) {
                // Sync data with the client
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }

            if (itemTickTimer.tick()) {
                handleInputItemTicking();
            }
        }
    }

    private void handleInputItemTicking() {
        CookingFoodTickerResult result = cookingFoodTicker.tick(getInputItemStack(), getFluidStack(), heatLevel, false, false, true);
        if (result.updated) {
            if (fluids[FLUID_PRIMARY].amount == 0) {
                fluids[FLUID_PRIMARY] = null;
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();

            clientNeedToUpdate = true;
        }
    }

    private void checkRemainingHeat() {
        if (heatLevel != CookingHeatLevel.NONE) {
            if (heatedUntilTicks < TFC_Time.getTotalTicks()) {
                heatLevel = CookingHeatLevel.values()[heatLevel.ordinal() - 1];

                onHeatLevelChanged();

                if (heatLevel != CookingHeatLevel.NONE) {
                    heatedUntilTicks = TFC_Time.getTotalTicks() + CookingPitHelper.COOLING_DOWN_TICKS;
                }

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }
        }
    }

    private void findAndConsumeHeatingStones() {
        if (hasFluid()) {
            EntityScanner scanner = new EntityScanner(worldObj, xCoord, yCoord, zCoord);
            for (EntityItem ei : scanner.getEntities()) {
                if (ei.getEntityItem().getItem() instanceof ItemHeatingStone && ei.getEntityItem().getItemDamage() == HeatingStoneMeta.HOT) {
                    if (heatLevel == CookingHeatLevel.NONE) {
                        heatLevel = CookingHeatLevel.LOW;

                        onHeatLevelChanged();
                    } else if (heatLevel != getMaxHeatLevel()) {
                        long ticksRemaining = heatedUntilTicks - TFC_Time.getTotalTicks();
                        long ticksSpent = CookingPitHelper.TICKS_PER_HOT_STONE - ticksRemaining;
                        if (ticksSpent < CookingPitHelper.TICKS_SPENT_THRESHOLD_TO_ALLOW_INCREASE) {
                            heatLevel = CookingHeatLevel.values()[heatLevel.ordinal() + 1];

                            onHeatLevelChanged();
                        }
                    }

                    heatedUntilTicks = TFC_Time.getTotalTicks() + CookingPitHelper.TICKS_PER_HOT_STONE;

                    ei.setDead();

                    onHeatingStoneConsumed();

                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    markDirty();
                }
            }
        }
    }

    protected void onHeatingStoneConsumed() {
        if (storage[SLOT_STONES] == null) {
            storage[SLOT_STONES] = new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.COLD);
        } else {
            storage[SLOT_STONES].stackSize++;
        }
    }

    protected void onHeatLevelChanged() {
        Bids.LOG.debug("Heat level changed: " + heatLevel);

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        cookingEngine.onRecipeParametersChanged(false);
    }

    protected abstract CookingHeatLevel getMaxHeatLevel();

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDataToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        // This forces client render after NBT data is loaded
        // until then the cooking pit cannot be fully rendered
        if (worldObj.isRemote && !clientDataLoaded) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientDataLoaded = true;
        }

        readDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readDataFromNBT(tag);
    }

    public void writeDataToNBT(NBTTagCompound tag) {
        if (heatLevel != CookingHeatLevel.NONE) {
            tag.setString("heatLevel", heatLevel.name());
            tag.setLong("heatedUntilTicks", heatedUntilTicks);
        }

        if (hasInputItem()) {
            NBTTagList itemTagList = new NBTTagList();
            for (int i = 0; i < MAX_STORAGE; i++) {
                if (storage[i] != null) {
                    NBTTagCompound itemTag = new NBTTagCompound();
                    itemTag.setInteger("slot", i);
                    storage[i].writeToNBT(itemTag);
                    itemTagList.appendTag(itemTag);
                }
            }
            tag.setTag("storage", itemTagList);
        }

        if (hasFluid()) {
            NBTTagList fluidTagList = new NBTTagList();
            for (int i = 0; i < MAX_FLUIDS; i++) {
                if (fluids[i] != null) {
                    NBTTagCompound fluidTag = new NBTTagCompound();
                    fluidTag.setInteger("slot", i);
                    fluids[i].writeToNBT(fluidTag);
                    fluidTagList.appendTag(fluidTag);
                }
            }
            tag.setTag("fluids", fluidTagList);
        }

        cookingEngine.writeDataToNBT(tag);
    }

    public void readDataFromNBT(NBTTagCompound tag) {
        try {
            heatLevel = CookingHeatLevel.valueOf(tag.getString("heatLevel"));
        } catch (IllegalArgumentException e) {
            heatLevel = CookingHeatLevel.NONE;
        }

        if (heatLevel != CookingHeatLevel.NONE) {
            heatedUntilTicks = tag.getLong("heatedUntilTicks");
        } else {
            heatedUntilTicks = 0;
        }

        {
            for (int i = 0; i < MAX_STORAGE; i++) {
                storage[i] = null;
            }

            NBTTagList itemTagList = tag.getTagList("storage", 10);
            for (int i = 0; i < itemTagList.tagCount(); i++) {
                NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
                final int slot = itemTag.getInteger("slot");
                storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }

        {
            for (int i = 0; i < MAX_FLUIDS; i++) {
                fluids[i] = null;
            }

            NBTTagList fluidTagList = tag.getTagList("fluids", 10);
            for (int i = 0; i < fluidTagList.tagCount(); i++) {
                NBTTagCompound fluidTag = fluidTagList.getCompoundTagAt(i);
                final int slot = fluidTag.getInteger("slot");
                fluids[slot] = FluidStack.loadFluidStackFromNBT(fluidTag);
            }
        }

        cookingEngine.readDataFromNBT(tag);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return storage[SLOT_INPUT];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (itemStack == null) {
            if (storage[SLOT_INPUT] != null) {
                // Item has decayed out of existence
                storage[SLOT_INPUT] = null;

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;
            }
        } else {
            storage[SLOT_INPUT] = itemStack;
        }
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public void handleNetworkPacket(SimpleUpdatePacket packet) {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Bids.LOG.debug("Client updated at: [{},{},{}]", xCoord, yCoord, zCoord);
    }

    public void sendUpdateMessage() {
        Network.sendToTileEntity(new SimpleUpdatePacket(), this);
        Bids.LOG.debug("Sent update message");
    }

}
