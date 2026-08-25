package com.unforbidable.tfc.bids.features.device.dryingframe.tileentity;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.features.drying.DryingFrameRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRecipe;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingEngine;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingHelper;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingHost;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingItem;
import com.unforbidable.tfc.bids.features.device.dryingframe.DryingFrameRegistry;
import com.unforbidable.tfc.bids.features.device.dryingframe.main.DryingFrameItem;
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
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDryingPegs extends TileEntity implements IInventory, DryingHost, PacketHandler<SimpleUpdatePacket> {

    private static final int MAX_STORAGE = 1;
    private static final int SLOT_ITEM = 0;

    private static final long DRYING_INTERVAL = 100;
    private static final int DRYING_TIMER_INTERVAL = 20;

    DryingFrameItem[] storage = new DryingFrameItem[MAX_STORAGE];

    Timer dryingTimer = new Timer(DRYING_TIMER_INTERVAL);

    long lastDryingTicks = 0;

    boolean clientNeedToUpdate = false;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage();

                clientNeedToUpdate = false;
            }

            TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, false);

            // Check if enough time had passed
            // for drying interval
            if (dryingTimer.tick() && TFC_Time.getTotalTicks() > lastDryingTicks + DRYING_INTERVAL) {
                new DryingEngine(this).update();

                lastDryingTicks = TFC_Time.getTotalTicks();
            }
        }
    }

    public void placeItemStack(ItemStack itemStack, ItemStack cordage, EntityPlayer player) {
        if (storage[SLOT_ITEM] == null) {
            ItemStack input = itemStack.copy();
            input.stackSize = 1;

            DryingFrameItem dryingFrameItem = new DryingFrameItem();
            dryingFrameItem.inputItem = input;
            dryingFrameItem.tyingItem = cordage;

            DryingFrameRecipe recipe = getRecipeForInputItem(itemStack);
            DryingHelper.initializeInputItem(dryingFrameItem, recipe);

            storage[SLOT_ITEM] = dryingFrameItem;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            clientNeedToUpdate = true;
        }
    }

    public boolean hasItem() {
        return storage[SLOT_ITEM] != null;
    }

    public DryingFrameItem getItem() {
        return storage[SLOT_ITEM];
    }

    public void onDryingPegsBroken() {
        for (DryingFrameItem is : storage) {
            if (is != null) {
                EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is.getCurrentItem());
                ei.motionX = 0;
                ei.motionZ = 0;
                worldObj.spawnEntityInWorld(ei);

                EntityItem ei2 = new EntityItem(worldObj, xCoord, yCoord, zCoord, is.tyingItem);
                ei2.motionX = 0;
                ei2.motionZ = 0;
                worldObj.spawnEntityInWorld(ei2);
            }
        }
    }

    private DryingFrameRecipe getRecipeForInputItem(ItemStack inputItem) {
        return DryingFrameRegistry.recipes.findMatchingRecipe(inputItem);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 1, zCoord + 2);
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

    public void readTileEntityDataFromNBT(NBTTagCompound tag) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = DryingFrameItem.loadDryingFrameItemFromNBT(itemTag);
        }
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

    @Override
    public int getSizeInventory() {
        return MAX_STORAGE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        // This is called by TFC to retrieve items for decay calc etc
        return storage[slot] != null ? storage[slot].getCurrentItem() : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        // This is called by TFC to return items after decay calc etc
        if (itemStack == null) {
            if (storage[slot] != null) {
                // Item has decayed out of existence
                storage[slot] = null;

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                clientNeedToUpdate = true;
            } else {
                // null for null returned
            }
        } else {
            DryingFrameItem prev = storage[slot];

            if (prev != null) {
                storage[slot].updateCurrentItem(itemStack);
            } else {
                // This should not happen
                Bids.LOG.warn("TFC returned an item after decay calculation into a slot that is empty.");
            }
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
    public DryingItem[] getDryingStorage() {
        return storage;
    }

    @Override
    public DryingRecipe getDryingRecipe(DryingItem item) {
        return getRecipeForInputItem(item.inputItem);
    }

    @Override
    public float getWetnessIncreaseRate() {
        return 0.2f;
    }

    @Override
    public float getWetnessReductionRate() {
        return 0.6f;
    }

    @Override
    public void notifyClientChanges() {
        clientNeedToUpdate = true;
    }

}
