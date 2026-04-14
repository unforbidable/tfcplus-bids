package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drying.DryingEngine;
import com.unforbidable.tfc.bids.Core.Drying.DryingHelper;
import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.Core.Drying.IDryingHost;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.DryingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.DryingSurfaceRecipe;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.world.World;

public class TileEntityDryingSurface extends TileEntity implements IInventory, IMessageHanldingTileEntity<TileEntityUpdateMessage>, IDryingHost {

    public static final int MAX_STORAGE = 4;

    private static final long DRYING_INTERVAL = 50;
    private static final int DRYING_TIMER_INTERVAL = 10;

    private final DryingItem[] storage = new DryingItem[MAX_STORAGE];

    private int selectedSlot = -1;

    long lastDryingTicks = 0;
    boolean clientNeedToUpdate = false;

    Timer dryingTimer = new Timer(DRYING_TIMER_INTERVAL);
    Timer decayTimer = new Timer(100);

    boolean initialized = false;

    public void setSelectedSlot(int selectedSlot) {
        if (this.selectedSlot != selectedSlot) {
            this.selectedSlot = selectedSlot;
        }
    }

    public DryingItem getSelectedItem() {
        if (selectedSlot != -1) {
            return getItem(selectedSlot);
        }

        return null;
    }

    public DryingItem getItem(int section) {
        return storage[section];
    }

    public void clearSelectedSection() {
        selectedSlot = -1;
    }

    public int getSelectedSection() {
        return selectedSlot;
    }


    public boolean isInitialized() {
        return initialized;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1.5, zCoord + 1);
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // One time only right after creation
            if (!initialized) {
                initialized = true;
            }

            if (decayTimer.tick()) {
                TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, false);
            }

            // Check if enough time had passed
            // for drying interval
            if (dryingTimer.tick() && TFC_Time.getTotalTicks() > lastDryingTicks + DRYING_INTERVAL) {
                DryingEngine engine = new DryingEngine(this);
                if (engine.update()) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                    clientNeedToUpdate = true;
                }

                lastDryingTicks = TFC_Time.getTotalTicks();
            }

            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }
        }
    }

    private DryingSurfaceRecipe getRecipeForInputItem(ItemStack inputItem) {
        return DryingSurfaceManager.getMatchingRecipe(inputItem);
    }

    public ItemStack getSlotActualItem(int slot) {
        if (storage[slot] != null) {
            return storage[slot].getCurrentItem();
        } else {
            return null;
        }
    }

    public boolean canPlaceItem(ItemStack itemStack) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                return true;
            }
        }

        return false;
    }

    public boolean placeItem(ItemStack itemStack) {
        Bids.LOG.debug("Placing item on drying rack: " + itemStack.getDisplayName());
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                ItemStack inputItem = itemStack.copy();
                inputItem.stackSize = 1;

                DryingItem dryingItem = new DryingItem();
                dryingItem.inputItem = inputItem;
                dryingItem.progress = 0;
                dryingItem.lastProgressUpdatedTicks = TFC_Time.getTotalTicks();

                DryingSurfaceRecipe recipe = getRecipeForInputItem(itemStack);
                if (recipe != null) {
                    // If input item already has some progress done
                    // we take this progress and move the start ticks back accordingly
                    DryingHelper.initializeInputItemProgress(dryingItem, recipe);

                    if (dryingItem.progress == 1) {
                        // Should progress initialize to complete, set the result
                        dryingItem.resultItem = DryingHelper.getResultItem(dryingItem, recipe);
                    }
                } else {
                    // For items without a recipe only wetness will be handled
                    DryingHelper.initializeInputItemWetness(dryingItem);
                }

                storage[i] = dryingItem;

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;

                return true;
            }
        }

        return false;
    }

    public void onDryingSurfaceBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                ItemStack currentItem = storage[i].getCurrentItem();
                if (currentItem != null) {
                    EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, currentItem);
                    worldObj.spawnEntityInWorld(ei);
                }
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private int getTotalItems() {
        int count = 0;
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                count++;
            }
        }

        return count;
    }

    public boolean retrieveItem(int section, EntityPlayer player) {
        Bids.LOG.debug("Retrieving item from drying rack, section: " + section);

        if (section >= 0 && section < MAX_STORAGE && storage[section] != null) {
            ItemStack currentItem = storage[section].getCurrentItem();
            if (currentItem != null) {
                EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, currentItem);
                worldObj.spawnEntityInWorld(ei);
            }

            storage[section] = null;

            if (getTotalItems() > 0) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                clientNeedToUpdate = true;
            } else {
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }

            return true;
        }

        return false;
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeSoakingSurfaceDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readSoakingSurfaceDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeSoakingSurfaceDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readSoakingSurfaceDataFromNBT(tag);
    }

    public void writeSoakingSurfaceDataToNBT(NBTTagCompound tag) {
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

    public void readSoakingSurfaceDataFromNBT(NBTTagCompound tag) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = DryingItem.loadDryingItemFromNBT(itemTag);
        }
    }


    @Override
    public void onTileEntityMessage(TileEntityUpdateMessage message) {
        worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
        Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
            + message.getZCoord());
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new TileEntityUpdateMessage(x, y, z, 0), tp);
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
            DryingItem prev = storage[slot];

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
        return isSoilSurface() ? 0.6f : 0.4f;
    }

    @Override
    public float getWetnessReductionRate() {
        return isSoilSurface() ? 0.2f : 0.4f;
    }

    private boolean isSoilSurface() {
        return TFC_Core.isSoil(worldObj.getBlock(xCoord, yCoord - 1, zCoord));
    }

}
