package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.TileEntities.TEAxle;
import com.dunk.tfc.TileEntities.TERotator;
import com.unforbidable.tfc.bids.api.Interfaces.IScrewLoadProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityScrew extends TEAxle {

    @Override
    public float getLoad(TERotator carrier, boolean flipParity, float speedMultiplier) {
        float load = 0;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (te instanceof IScrewLoadProvider) {
                load += ((IScrewLoadProvider) te).getLoadForScrewInDirection(d.getOpposite());
            }
        }

        return super.getLoad(carrier, flipParity, speedMultiplier) + load * speedMultiplier;
    }

    public float getScrewSpin() {
        // The screw handedness is currently not considered
        // otherwise we would flip the spin when applicable
        return Math.abs(getSpin());
    }

}
