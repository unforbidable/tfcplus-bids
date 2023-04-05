package com.unforbidable.tfc.bids.Core.Carving.Modes;

import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingMode;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

import static com.unforbidable.tfc.bids.TileEntities.TileEntityCarving.CARVING_DIMENSION;

public class CarvingModeSide implements ICarvingMode {

    @Override
    public String getName() {
        return "SIDE";
    }

    @Override
    public boolean canCarveBit(CarvingBit selectedBit, int side, CarvingBitMap currentCarvedBits) {
        // Determine the current edges
        int minX = 4;
        int maxX = -1;
        int minY = 4;
        int maxY = -1;
        int minZ = 4;
        int maxZ = -1;
        for (int x = 0; x < CARVING_DIMENSION; x++) {
            for (int y = 0; y < CARVING_DIMENSION; y++) {
                for (int z = 0; z < CARVING_DIMENSION; z++) {
                    if (!currentCarvedBits.testBit(x, y, z)) {
                        // Not carved, so the min and max will be adjusted
                        minX = Math.min(minX, x);
                        maxX = Math.max(maxX, x);
                        minY = Math.min(minY, y);
                        maxY = Math.max(maxY, y);
                        minZ = Math.min(minZ, z);
                        maxZ = Math.max(maxZ, z);
                    }
                }
            }
        }

        // Can carve if carved bit is at the current edge of the selected side
        ForgeDirection d = ForgeDirection.getOrientation(side);

        switch (d) {
            case UP:
                return selectedBit.bitY == maxY;
            case DOWN:
                return selectedBit.bitY == minY;
            case NORTH:
                return selectedBit.bitZ == minZ;
            case SOUTH:
                return selectedBit.bitZ == maxZ;
            case WEST:
                return selectedBit.bitX == minX;
            case EAST:
                return selectedBit.bitX == maxX;
        }

        return false;
    }

    @Override
    public List<CarvingBit> getBitsToCarve(CarvingBit selectedBit, int side, CarvingBitMap currentCarvedBits) {
        // Start with all the ranges
        int minX = 0;
        int maxX = 4;
        int minY = 0;
        int maxY = 4;
        int minZ = 0;
        int maxZ = 4;

        // Then limit the ranges to the slice that contains the selected bit
        // depending on the selected side
        ForgeDirection d = ForgeDirection.getOrientation(side);
        switch (d) {
            case UP:
            case DOWN:
                minY = selectedBit.bitY;
                maxY = minY + 1;
                break;

            case NORTH:
            case SOUTH:
                minZ = selectedBit.bitZ;
                maxZ = minZ + 1;
                break;

            case EAST:
            case WEST:
                minX = selectedBit.bitX;
                maxX = minX + 1;
                break;
        }

        List<CarvingBit> bitsToCarve = new ArrayList<CarvingBit>();

        // Add bits to carve from the determined slice
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    bitsToCarve.add(new CarvingBit(x, y, z));
                }
            }
        }

        return bitsToCarve;
    }

    @Override
    public AxisAlignedBB getSelectedBitBounds(CarvingBit selectedBit, int side) {
        final float div = 1f / TileEntityCarving.CARVING_DIMENSION;

        // Start with the whole block
        float minX = 0;
        float maxX = 1;
        float minY = 0;
        float maxY = 1;
        float minZ = 0;
        float maxZ = 1;

        // Then limit the selection to the slice that contains the selected bit
        // depending on the selected side
        ForgeDirection d = ForgeDirection.getOrientation(side);
        switch (d) {
            case UP:
            case DOWN:
                minY = selectedBit.bitY * div;
                maxY = minY + div;
                break;

            case NORTH:
            case SOUTH:
                minZ = selectedBit.bitZ * div;
                maxZ = minZ + div;
                break;

            case EAST:
            case WEST:
                minX = selectedBit.bitX * div;
                maxX = minX + div;
                break;
        }

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
