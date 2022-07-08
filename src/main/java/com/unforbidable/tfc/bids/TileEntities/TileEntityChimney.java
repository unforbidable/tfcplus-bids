package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IChimney;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityChimney extends TileEntity implements IChimney {

    int smoke = 0;

    public TileEntityChimney() {
        super();
    }

    @Override
    public int getChimneyTier() {
        return 0;
    }

    @Override
    public void setChimneySmoke(int smoke) {
        this.smoke = smoke;
    }

    @Override
    public int getChimneySmoke() {
        return smoke;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (yCoord < 255) {
                // Push smoke to entity above
                TileEntity above = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
                if (smoke > 0 && ChimneyHelper.isChimney(above)
                        && ChimneyHelper.getChimneySmoke(above) < smoke) {
                    ChimneyHelper.setChimneySmoke(above, smoke);
                    smoke = 0;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }

            if (smoke > 0) {
                smoke--;
                if (smoke == 0) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        readChimneyDataFromNBT(tag);
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeChimneyDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        writeChimneyDataToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readChimneyDataFromNBT(tag);
    }

    public void writeChimneyDataToNBT(NBTTagCompound tag) {
        tag.setInteger("smoke", smoke);
    }

    public void readChimneyDataFromNBT(NBTTagCompound tag) {
        smoke = tag.getInteger("smoke");
    }

}
