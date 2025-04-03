package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.DryingRack.*;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager.TyingEquipment;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
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

public class TileEntityDryingRack extends TileEntity
        implements IInventory, IMessageHanldingTileEntity<DryingRackMessage> {

    public static final int MAX_STORAGE = 4;

    public static final int ACTION_UPDATE = 1;

    static final long DRYING_INTERVAL = TFC_Time.HOUR_LENGTH / 12;
    private static final int DRYING_TIMER_INTERVAL = 20;

    final DryingRackItem[] storage = new DryingRackItem[MAX_STORAGE];

    long lastDryingTicks = 0;
    boolean initialized;
    int orientation = -1;
    boolean cordless = false;

    boolean clientNeedToUpdate = false;

    int selectedSection = -1;

    Timer dryingTimer = new Timer(DRYING_TIMER_INTERVAL);
    Timer decayTimer = new Timer(100);

    public TileEntityDryingRack() {
        super();
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isCordless() {
        return cordless;
    }

    public void setCordless(boolean cordless) {
        this.cordless = cordless;
    }

    public void setSelectedSection(int selectedSection) {
        this.selectedSection = selectedSection;
    }

    public void clearSelectedSection() {
        selectedSection = -1;
    }

    public int getSelectedSection() {
        return selectedSection;
    }

    public DryingRackItem getSelectedItem() {
        if (selectedSection != -1) {
            return getItem(selectedSection);
        }

        return null;
    }

    public DryingRackItem getItem(int section) {
        return storage[section];
    }

    public DryingRackItemInfo getSelectedItemInfo() {
        if (selectedSection != -1) {
            return getItemInfo(selectedSection);
        }

        return null;
    }

    public DryingRackItemInfo getItemInfo(int section) {
        if (storage[section] != null) {
            final DryingRecipe recipe = DryingManager.getMatchingRecipe(storage[section].dryingItem);

            if (recipe != null) {
                final long total = recipe.getDuration() * TFC_Time.HOUR_LENGTH;
                final long elapsed = (int) (TFC_Time.getTotalTicks() - storage[section].dryingStartTicks);
                final int dryingTicksRemaining = Math.max((int) (total - elapsed), 0);

                return new DryingRackItemInfo(storage[section], recipe, dryingTicksRemaining);
            }
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // One time only right after creation
            if (!initialized) {
                lastDryingTicks = TFC_Time.getTotalTicks();

                initialized = true;
            }

            if (decayTimer.tick()) {
                TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, false);
            }

            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            // Check if enough time had passed
            // for drying interval
            if (dryingTimer.tick() && TFC_Time.getTotalTicks() > lastDryingTicks + DRYING_INTERVAL) {
                dryItems();

                lastDryingTicks = TFC_Time.getTotalTicks();
            }
        }
    }

    private void dryItems() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                // Check time passed since item was added
                // if it is greater than drying time
                // according to the matching recipe

                final DryingRackItem item = storage[i];
                final DryingRecipe recipe = DryingManager.getMatchingRecipe(item.dryingItem);

                if (recipe != null) {
                    final long ticksElapsedTotal = TFC_Time.getTotalTicks() - item.dryingStartTicks;
                    final long ticksLastDelta = TFC_Time.getTotalTicks() - lastDryingTicks;

                    // This is for recipes that track progress or update items over time
                    final long durationTicks = recipe.getDuration() * TFC_Time.HOUR_LENGTH;
                    final float progressTotal = (float) ticksElapsedTotal / durationTicks;
                    final float progressTotalDelta = (float) ticksLastDelta / durationTicks;
                    recipe.onProgress(item.dryingItem, progressTotal, progressTotalDelta);

                    if (ticksElapsedTotal > recipe.getDuration() * TFC_Time.HOUR_LENGTH) {
                        ItemStack driedItem = recipe.getCraftingResult(item.dryingItem).copy();

                        Bids.LOG.debug("Item " + item.dryingItem.getDisplayName()
                                + " dried and become " + driedItem.getDisplayName());

                        if (item.dryingItem.getItem() instanceof ItemFoodTFC) {
                            Bids.LOG.debug("Weight from: " + Food.getWeight(item.dryingItem)
                                    + " to: " + Food.getWeight(driedItem));
                        }

                        storage[i] = new DryingRackItem(driedItem, item.tyingItem, item.dryingStartTicks, true);

                        clientNeedToUpdate = true;

                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }

                } else {
                    // Item has no matching recipe
                    // so either item is already dried
                    // or the recipe no longer exists
                    // or it's clothes
                    dryClothes();
                }
            }
        }
    }

    private void dryClothes() {
        for (DryingRackItem itemStack : storage) {
            if (itemStack != null && itemStack.dryingItem.getItem() instanceof ItemClothing) {
                float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);
                if (TFC_Core.isExposedToRain(worldObj, xCoord, yCoord, zCoord) && temp > 0) {
                    DryingRackHelper.handleClothesInRain(itemStack.dryingItem, DRYING_TIMER_INTERVAL);
                } else if (temp > 0) {
                    DryingRackHelper.handleNormalDry(worldObj, xCoord, yCoord, zCoord, itemStack.dryingItem, DRYING_TIMER_INTERVAL);
                }
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDryingRackDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readDryingRackDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeDryingRackDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readDryingRackDataFromNBT(tag);
    }

    public void writeDryingRackDataToNBT(NBTTagCompound tag) {
        tag.setLong("lastDryingTicks", lastDryingTicks);
        tag.setBoolean("clientInitialized", initialized);
        tag.setInteger("orientation", orientation);
        tag.setBoolean("cordless", cordless);

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

    public void readDryingRackDataFromNBT(NBTTagCompound tag) {
        lastDryingTicks = tag.getLong("lastDryingTicks");
        initialized = tag.getBoolean("clientInitialized");
        orientation = tag.getInteger("orientation");
        cordless = tag.getBoolean("cordless");

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = DryingRackItem.loadItemFromNBT(itemTag);
        }
    }

    public boolean placeItem(int section, EntityPlayer player, ItemStack itemStack) {
        Bids.LOG.debug("Placing item on drying rack: " + itemStack.getDisplayName() + ", section: " + section);

        DryingRecipe recipe = DryingManager.getMatchingRecipe(itemStack);

        if (section >= 0 && section < MAX_STORAGE
                && storage[section] == null) {
            if (recipe != null) {
                final boolean consumeTyingEquipment = recipe.getRequiresTyingEquipment();
                ItemStack tyingEquipment = null;

                if (consumeTyingEquipment) {
                    tyingEquipment = findAndConsumeTyingEquipment(player);
                }

                if (consumeTyingEquipment && tyingEquipment == null) {
                    // Tying equipment needed and missing
                    return false;
                }

                // Copy item to preserve NBT
                ItemStack dryingItem = itemStack.copy();
                dryingItem.stackSize = 1;

                // If drying item already has some progress done
                // we take this progress and move the start ticks back accordingly
                final float initialProgress = recipe.getInitialProgress(itemStack);
                final int initialTicks = Math.round(initialProgress * recipe.getDuration() * TFC_Time.HOUR_LENGTH);
                final long startTicks = TFC_Time.getTotalTicks() - initialTicks;

                storage[section] = new DryingRackItem(dryingItem, tyingEquipment, startTicks, false);

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                clientNeedToUpdate = true;

                return true;
            } else if (itemStack.getItem() instanceof ItemClothing) {
                ItemStack dryingItem = itemStack.copy();
                dryingItem.stackSize = 1;

                storage[section] = new DryingRackItem(dryingItem, null, 0, false);

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                clientNeedToUpdate = true;

                return true;
            }
        }

        return false;
    }

    public void onDryingRackBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                if (storage[i].dryingItem != null) {
                    final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                            storage[i].dryingItem);
                    worldObj.spawnEntityInWorld(ei);
                }

                if (canDropTyingEquipment(i)) {
                    final EntityItem ei2 = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                            storage[i].tyingItem);
                    worldObj.spawnEntityInWorld(ei2);
                }
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean retrieveItem(int section, EntityPlayer player) {
        Bids.LOG.debug("Retrieving item from drying rack, section: " + section);

        if (section >= 0 && section < MAX_STORAGE
                && storage[section] != null) {
            if (storage[section].dryingItem != null) {
                final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ,
                        storage[section].dryingItem);
                worldObj.spawnEntityInWorld(ei);
            }

            if (canDropTyingEquipment(section)) {
                final EntityItem ei2 = new EntityItem(worldObj, player.posX, player.posY, player.posZ,
                        storage[section].tyingItem);
                worldObj.spawnEntityInWorld(ei2);
            }

            storage[section] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            clientNeedToUpdate = true;

            return true;
        }

        return false;
    }

    private boolean canDropTyingEquipment(int i) {
        if (storage[i].tyingItem != null) {
            final TyingEquipment te = DryingManager.findTyingEquipmnt(storage[i].tyingItem);
            if (te != null && te.isReusable) {
                // Reusable tying equipment will drop even when
                // technically "used up"
                return true;
            }

            return !storage[i].tyingItemUsedUp;
        }

        return false;
    }

    private ItemStack findAndConsumeTyingEquipment(EntityPlayer player) {
        // Reusable tying equipment takes priority
        // Search is done in order of registration

        for (DryingManager.TyingEquipment tyingEquipment : DryingManager.getTyingEquipment()) {
            if (tyingEquipment.isReusable) {
                ItemStack result = findAndConsumeOneTyingEquipment(player, tyingEquipment.item);
                if (result != null) {
                    return result;
                }
            }
        }

        for (DryingManager.TyingEquipment tyingEquipment : DryingManager.getTyingEquipment()) {
            if (!tyingEquipment.isReusable) {
                ItemStack result = findAndConsumeOneTyingEquipment(player, tyingEquipment.item);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    private ItemStack findAndConsumeOneTyingEquipment(EntityPlayer player, ItemStack item) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            final ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.getItem() == item.getItem()
                    && is.getItemDamage() == item.getItemDamage()) {

                final ItemStack copy = is.copy();
                copy.stackSize = 1;

                player.inventory.consumeInventoryItem(is.getItem());
                player.inventoryContainer.detectAndSendChanges();

                Bids.LOG.debug("Found and consumed tying equipment: " + copy.getDisplayName());

                return copy;
            }
        }

        return null;
    }

    @Override
    public void onTileEntityMessage(DryingRackMessage message) {
        switch (message.getAction()) {
            case ACTION_UPDATE:
                worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
                Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
                        + message.getZCoord());
                break;
        }
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        TargetPoint tp = new TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new DryingRackMessage(x, y, z, TileEntityDryingRack.ACTION_UPDATE), tp);
        Bids.LOG.debug("Sent update message");
    }

    @Override
    public int getSizeInventory() {
        return MAX_STORAGE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        // This is called by TFC to retrieve items for decay calc etc
        return storage[slot] != null ? storage[slot].dryingItem : null;
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
            DryingRackItem prev = storage[slot];

            if (prev != null) {
                storage[slot] = new DryingRackItem(itemStack, prev.tyingItem, prev.dryingStartTicks,
                        prev.tyingItemUsedUp);
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

}
