package com.unforbidable.tfc.bids.Core.Chimney;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.TEChimney;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.api.Interfaces.IChimney;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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
        if (tileEntity != null) {
            TileEntity top = getTopMostChimney(tileEntity);
            return TFC_Core.isExposed(top.getWorldObj(), top.xCoord, top.yCoord, top.zCoord);
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

    public static TileEntityCrucible findActiveFurnaceCrucible(TileEntity tileEntity) {
        ForgeDirection checkList[] = { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH,
                ForgeDirection.WEST };

        for (ForgeDirection dir : checkList) {
            TileEntity te = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + dir.offsetX, tileEntity.yCoord,
                    tileEntity.zCoord + dir.offsetZ);
            if (te != null && te instanceof TileEntityCrucible) {
                TileEntityCrucible crucible = (TileEntityCrucible) te;
                if (crucible.isGlassMakingActive()) {
                    return crucible;
                }
            }
        }

        return null;
    }

}
