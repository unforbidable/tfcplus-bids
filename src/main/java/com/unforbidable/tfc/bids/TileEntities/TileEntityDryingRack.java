package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drying.*;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItem;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.Crafting.DryingRackManager;
import com.unforbidable.tfc.bids.api.Crafting.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
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
        implements IInventory, IMessageHanldingTileEntity<TileEntityUpdateMessage>, IDryingHost {

    public static final int MAX_STORAGE = 4;
    private static final long DRYING_INTERVAL = 100;
    private static final int DRYING_TIMER_INTERVAL = 20;

    final DryingRackItem[] storage = new DryingRackItem[MAX_STORAGE];

    long lastDryingTicks = 0;
    int orientation = -1;
    ItemStack cordage = null;

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

    public void setCordage(ItemStack itemStack) {
        this.cordage = itemStack;
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

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            if (decayTimer.tick()) {
                TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, false);
            }

            // Check if enough time had passed
            // for drying interval
            if (dryingTimer.tick() && TFC_Time.getTotalTicks() > lastDryingTicks + DRYING_INTERVAL) {
                DryingEngineUpdate update = new DryingEngine(this).update();
                if (update.data) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                    if (update.item) {
                        clientNeedToUpdate = true;
                    }
                }

                lastDryingTicks = TFC_Time.getTotalTicks();
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
        tag.setInteger("orientation", orientation);

        if (cordage != null) {
            NBTTagCompound itemTag = new NBTTagCompound();
            cordage.writeToNBT(itemTag);
            tag.setTag("cordage", itemTag);
        }

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
        orientation = tag.getInteger("orientation");

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = DryingRackItem.loadDryingRackItemFromNBT(itemTag);
        }

        cordage = null;
        if (tag.hasKey("cordage")) {
            NBTTagCompound itemTag = tag.getCompoundTag("cordage");
            cordage = ItemStack.loadItemStackFromNBT(itemTag);
        }
    }

    private DryingRackRecipe getRecipeForInputItem(ItemStack inputItem) {
        return DryingRackManager.getMatchingRecipe(inputItem);
    }

    public boolean placeItem(int section, EntityPlayer player, ItemStack itemStack) {
        Bids.LOG.debug("Placing item on drying rack: " + itemStack.getDisplayName() + ", section: " + section);

        if (section >= 0 && section < MAX_STORAGE && storage[section] == null) {
            ItemStack inputItem = itemStack.copy();
            inputItem.stackSize = 1;

            DryingRackItem dryingRackItem = new DryingRackItem();
            dryingRackItem.inputItem = inputItem;

            DryingRackRecipe recipe = getRecipeForInputItem(itemStack);
            DryingHelper.initializeInputItem(dryingRackItem, recipe);

            if (recipe != null) {
                // For items with recipe check for tying equipment and initial progress
                boolean consumeTyingEquipment = recipe.getRequiresTyingEquipment();
                ItemStack tyingEquipment = consumeTyingEquipment ? findAndConsumeTyingEquipment(player) : null;

                if (consumeTyingEquipment && tyingEquipment == null) {
                    // Tying equipment needed and missing
                    return false;
                }

                dryingRackItem.tyingItem = tyingEquipment;
            } else {
                dryingRackItem.tyingItem = null;
            }

            storage[section] = dryingRackItem;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        }

        return false;
    }

    public void onDryingRackBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                ItemStack currentItem = storage[i].getCurrentItem();
                if (currentItem != null) {
                    EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, currentItem);
                    worldObj.spawnEntityInWorld(ei);
                }

                ItemStack currentTyingItem = storage[i].getCurrentTyingItem();
                if (currentTyingItem != null) {
                    EntityItem ei2 = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, currentTyingItem);
                    worldObj.spawnEntityInWorld(ei2);
                }
            }
        }

        if (cordage != null) {
            EntityItem ei3 = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, cordage);
            worldObj.spawnEntityInWorld(ei3);
        }

        ItemStack poles = new ItemStack(TFCItems.pole, 2);
        EntityItem ei4 = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, poles);
        worldObj.spawnEntityInWorld(ei4);
    }

    public boolean retrieveItem(int section, EntityPlayer player) {
        Bids.LOG.debug("Retrieving item from drying rack, section: " + section);

        if (section >= 0 && section < MAX_STORAGE && storage[section] != null) {
            ItemStack currentItem = storage[section].getCurrentItem();
            if (currentItem != null) {
                EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, currentItem);
                worldObj.spawnEntityInWorld(ei);
            }

            ItemStack currentTyingItem = storage[section].getCurrentTyingItem();
            if (currentTyingItem != null) {
                EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, currentTyingItem);
                worldObj.spawnEntityInWorld(ei);
            }

            storage[section] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            clientNeedToUpdate = true;

            return true;
        }

        return false;
    }

    private ItemStack findAndConsumeTyingEquipment(EntityPlayer player) {
        // Reusable tying equipment takes priority
        // Search is done in order of registration

        for (DryingRackManager.TyingEquipment tyingEquipment : DryingRackManager.getTyingEquipment()) {
            if (tyingEquipment.isReusable) {
                ItemStack result = findAndConsumeOneTyingEquipment(player, tyingEquipment.item);
                if (result != null) {
                    return result;
                }
            }
        }

        for (DryingRackManager.TyingEquipment tyingEquipment : DryingRackManager.getTyingEquipment()) {
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
    public void onTileEntityMessage(TileEntityUpdateMessage message) {
        worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
        Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
                + message.getZCoord());
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        TargetPoint tp = new TargetPoint(world.provider.dimensionId, x, y, z, 255);
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
            DryingRackItem prev = storage[slot];

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

}
