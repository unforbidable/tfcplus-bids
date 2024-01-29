package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressDiscPosition;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityScrewPressDisc extends TileEntity {

    public TileEntityScrewPressLever getScrewPressLeverTileEntity() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        int orientation = ScrewPressHelper.getOrientationFromMetadata(meta);
        for (ForgeDirection d : ScrewPressHelper.getNeighborDirectionsForOrientation(orientation)) {
            TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX * 2, yCoord, zCoord + d.offsetZ * 2);
            if (te instanceof TileEntityScrewPressLever) {
                return (TileEntityScrewPressLever) te;
            }
        }

        return null;
    }

    public TileEntityScrewPressBarrel getScrewPressBarrelTileEntity() {
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (te instanceof TileEntityScrewPressBarrel) {
            return (TileEntityScrewPressBarrel) te;
        } else {
            return null;
        }
    }

    public ScrewPressDiscPosition getDiscPosition(float f) {
        TileEntityScrewPressBarrel teBarrel = getScrewPressBarrelTileEntity();
        if (teBarrel != null) {
            return teBarrel.getDiscPosition(f);
        } else {
            return ScrewPressDiscPosition.INACTIVE;
        }
    }

}
