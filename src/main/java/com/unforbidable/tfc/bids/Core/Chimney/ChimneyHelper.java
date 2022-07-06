package com.unforbidable.tfc.bids.Core.Chimney;

import com.dunk.tfc.TileEntities.TEChimney;
import com.unforbidable.tfc.bids.api.Interfaces.IChimney;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ChimneyHelper {

    public static boolean isChimney(TileEntity tileEntity) {
        return tileEntity instanceof IChimney || tileEntity instanceof TEChimney;
    }

    public static int getChimneyTier(TileEntity tileEntity) {
        if (tileEntity instanceof IChimney) {
            return ((IChimney) tileEntity).getChimneyTier();
        } else if (tileEntity instanceof TEChimney) {
            return 1;
        }

        return -1;
    }

    public static boolean canChimneySeeSky(TileEntity tileEntity) {
        if (tileEntity instanceof IChimney) {
            return ((IChimney) tileEntity).canChimneySeeSky();
        } else if (tileEntity instanceof TEChimney) {
            return ((TEChimney) tileEntity).canChimneySeeSky();
        }

        return false;
    }

    public static TileEntity getTopMostChimney(TileEntity tileEntity) {
        TileEntity topMost = tileEntity;
        World worldObj = tileEntity.getWorldObj();
        while (tileEntity != null) {
            // Keep testing tile entity above until no chimney is found
            tileEntity = worldObj.getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord);
            if (tileEntity != null && isChimney(tileEntity)) {
                topMost = tileEntity;
            }
        }

        // Last chimney found is the top most one
        // It could be the original chimney
        return topMost;
    }

}
