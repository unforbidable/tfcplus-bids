package com.unforbidable.tfc.bids.TileEntities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDecorativeSurface extends TileEntity {

    private ItemStack item = null;

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void onDecorativeSurfaceBroken() {
        ItemStack is = getItem();
        if (is != null) {
            EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
            ei.motionX = 0;
            ei.motionZ = 0;
            worldObj.spawnEntityInWorld(ei);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
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
        NBTTagCompound itemTag = new NBTTagCompound();
        item.writeToNBT(itemTag);
        tag.setTag("item", itemTag);
    }

    public void readTileEntityDataFromNBT(NBTTagCompound tag) {
        NBTTagCompound itemTag = tag.getCompoundTag("item");
        item = ItemStack.loadItemStackFromNBT(itemTag);
    }

}
