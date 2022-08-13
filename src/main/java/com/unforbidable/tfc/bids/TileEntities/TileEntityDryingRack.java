package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDryingRack extends TileEntity {

    public static final int MAX_STORAGE = 4;

    static final long DRYING_INTERVAL = TFC_Time.HOUR_LENGTH;

    final ItemStack[] storage = new ItemStack[MAX_STORAGE];
    final ItemStack[] tyingEquipment = new ItemStack[MAX_STORAGE];

    long lastDryingTicks = 0;
    boolean initialized;
    int orientation = -1;

    int selectedSection = -1;

    Timer dryingTimer = new Timer(10);

    public TileEntityDryingRack() {
        super();
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
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

    public ItemStack getSelectedItem() {
        if (selectedSection != -1) {
            return storage[selectedSection];
        }

        return null;
    }

    public ItemStack getItem(int section) {
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
            // One time only right after creation
            if (!initialized) {
                lastDryingTicks = TFC_Time.getTotalTicks();

                initialized = true;
            }

            // Check if enough time had passed
            // for drying interval
            if (dryingTimer.tick() && TFC_Time.getTotalTicks() > lastDryingTicks + DRYING_INTERVAL) {
                dryItems();
            }
        }
    }

    private void dryItems() {
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

        NBTTagList itemTagListTying = new NBTTagList();
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (tyingEquipment[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("slot", i);
                tyingEquipment[i].writeToNBT(itemTag);
                itemTagListTying.appendTag(itemTag);
            }
        }
        tag.setTag("tyingEquipment", itemTagListTying);
    }

    public void readDryingRackDataFromNBT(NBTTagCompound tag) {
        lastDryingTicks = tag.getLong("lastDryingTicks");
        initialized = tag.getBoolean("clientInitialized");
        orientation = tag.getInteger("orientation");

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
            tyingEquipment[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }

        NBTTagList itemTagListTying = tag.getTagList("tyingEquipment", 10);
        for (int i = 0; i < itemTagListTying.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagListTying.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            tyingEquipment[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }
    }

    public boolean placeItem(int section, ItemStack itemStack) {
        Bids.LOG.debug("Placing item on drying rack: " + itemStack.getDisplayName() + ", section: " + section);

        if (section >= 0 && section < MAX_STORAGE
                && storage[section] == null) {
            storage[section] = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            return true;
        }

        return false;
    }

    public void onDryingRackBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                        storage[i]);
                worldObj.spawnEntityInWorld(ei);
            }

            if (tyingEquipment[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                        tyingEquipment[i]);
                worldObj.spawnEntityInWorld(ei);
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean retrieveItem(int section, EntityPlayer player) {
        Bids.LOG.debug("Retrieving item from drying rack, section: " + section);

        if (section >= 0 && section < MAX_STORAGE
                && storage[section] != null) {
            final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ,
                    storage[section]);
            worldObj.spawnEntityInWorld(ei);

            storage[section] = null;

            if (tyingEquipment[section] != null) {
                final EntityItem ei2 = new EntityItem(worldObj, player.posX, player.posY, player.posZ,
                        tyingEquipment[section]);
                worldObj.spawnEntityInWorld(ei2);

                tyingEquipment[section] = null;
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            return true;
        }

        return false;
    }

}
