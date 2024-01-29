package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Blocks.Devices.BlockAxleBearing;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressDiscPosition;
import com.unforbidable.tfc.bids.api.Interfaces.IScrewLoadProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityScrewPressLever extends TileEntity implements IScrewLoadProvider {

    private ForgeDirection screwDirection = ForgeDirection.UNKNOWN;

    public ForgeDirection getScrewDirection() {
        return screwDirection;
    }

    public void setScrewDirection(ForgeDirection screwDirection) {
        this.screwDirection = screwDirection;
    }

    public TileEntityScrewPressDisc getScrewPressDiscTileEntity() {
        if (screwDirection != ForgeDirection.UNKNOWN) {
            int discX = xCoord + screwDirection.getOpposite().offsetX * 2;
            int discY = yCoord;
            int discZ = zCoord + screwDirection.getOpposite().offsetZ * 2;
            TileEntity te = worldObj.getTileEntity(discX, discY, discZ);
            if (te instanceof TileEntityScrewPressDisc) {
                return (TileEntityScrewPressDisc) te;
            }
        }

        return null;
    }

    public TileEntityScrewPressBarrel getScrewPressBarrelTileEntity() {
        TileEntityScrewPressDisc teDisc = getScrewPressDiscTileEntity();
        if (teDisc != null) {
            return teDisc.getScrewPressBarrelTileEntity();
        } else {
            return null;
        }
    }

    public ScrewPressDiscPosition getDiscPosition(float partialTick) {
        TileEntityScrewPressBarrel teBarrel = getScrewPressBarrelTileEntity();
        if (teBarrel != null) {
            return teBarrel.getDiscPosition(partialTick);
        } else {
            return ScrewPressDiscPosition.INACTIVE;
        }
    }

    public TileEntityScrew getScrewTileEntity() {
        if (screwDirection != ForgeDirection.UNKNOWN) {
            int screwX = xCoord + screwDirection.offsetX;
            int screwY = yCoord;
            int screwZ = zCoord + screwDirection.offsetZ;
            TileEntity te = worldObj.getTileEntity(screwX, screwY, screwZ);
            TileEntity teAbove = worldObj.getTileEntity(screwX, screwY + 1, screwZ);
            if (te instanceof TileEntityScrew && teAbove instanceof TileEntityScrew) {
                return (TileEntityScrew) te;
            }
        }

        return null;
    }

    @Override
    public float getLoadForScrewInDirection(ForgeDirection direction) {
        // Is this our screw
        if (direction == screwDirection) {
            TileEntityScrew teScrew = getScrewTileEntity();
            if (teScrew != null) {
                int dir = BlockAxleBearing.getDirectionFromMetadata(teScrew.getBlockMetadata());
                // Is the screw vertically oriented
                // We ignore spin handedness, but it could be checked here
                if (dir == 2) {
                    TileEntityScrewPressBarrel teBarrel = getScrewPressBarrelTileEntity();
                    // Is there a basket and basin
                    if (teBarrel != null) {
                        float inputLoad = teBarrel.getInputLoad();
                        // No input load means the pressing disc is at the bottom of the barrel
                        // causing infinite load
                        return inputLoad > 0 ? inputLoad : Float.MAX_VALUE;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        switch (getScrewDirection()) {
            case WEST:
                return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord - 1, zCoord, xCoord + 4, yCoord + 2, zCoord + 1);
            case EAST:
                return AxisAlignedBB.getBoundingBox(xCoord - 4, yCoord - 1, zCoord, xCoord + 2, yCoord + 2, zCoord + 1);
            case NORTH:
                return AxisAlignedBB.getBoundingBox(xCoord, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 2, zCoord + 4);
            case SOUTH:
                return AxisAlignedBB.getBoundingBox(xCoord, yCoord - 1, zCoord - 4, xCoord + 1, yCoord + 2, zCoord + 2);
        }

        return super.getRenderBoundingBox();
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDataToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
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
        tag.setByte("screwDirection", (byte)screwDirection.ordinal());
    }

    public void readDataFromNBT(NBTTagCompound tag) {
        screwDirection = ForgeDirection.getOrientation(tag.getByte("screwDirection"));
    }

}
