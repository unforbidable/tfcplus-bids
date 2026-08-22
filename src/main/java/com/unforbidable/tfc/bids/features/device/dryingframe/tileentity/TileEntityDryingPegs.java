package com.unforbidable.tfc.bids.features.device.dryingframe.tileentity;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDryingPegs extends TileEntity implements PacketHandler<SimpleUpdatePacket> {

    private static final int MAX_STORAGE = 2;
    private static final int SLOT_ITEM = 0;
    private static final int SLOT_CORDAGE = 1;

    ItemStack[] storage = new ItemStack[MAX_STORAGE];

    boolean clientNeedToUpdate = false;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage();

                clientNeedToUpdate = false;
            }
        }
    }

    public void placeItemStack(ItemStack itemStack, ItemStack cordage, EntityPlayer player) {
        if (storage[SLOT_ITEM] == null) {
            storage[SLOT_ITEM] = itemStack.copy();
            storage[SLOT_ITEM].stackSize = 1;

            storage[SLOT_CORDAGE] = cordage;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            clientNeedToUpdate = true;
        }
    }

    public boolean hasItem() {
        return storage[SLOT_ITEM] != null;
    }

    public ItemStack getItem() {
        return storage[SLOT_ITEM];
    }

    public ItemStack getCordage() {
        return storage[SLOT_CORDAGE];
    }

    public void onDryingPegsBroken() {
        for (ItemStack is : storage) {
            if (is != null) {
                EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
                ei.motionX = 0;
                ei.motionZ = 0;
                worldObj.spawnEntityInWorld(ei);
            }
        }
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
            storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
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

}
