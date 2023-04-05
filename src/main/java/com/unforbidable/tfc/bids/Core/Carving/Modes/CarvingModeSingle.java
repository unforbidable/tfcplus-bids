package com.unforbidable.tfc.bids.Core.Carving.Modes;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingMode;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collections;
import java.util.List;

import static com.unforbidable.tfc.bids.TileEntities.TileEntityCarving.CARVING_DIMENSION;

public class CarvingModeSingle implements ICarvingMode {

    @Override
    public String getName() {
        return "SINGLE";
    }

    @Override
    public boolean canCarveBit(final CarvingBit bit, final int side, final CarvingBitMap carvedBits) {
        // Can be carved if bit is at the edge
        final int maxBit = CARVING_DIMENSION - 1;
        if (bit.bitX == 0 || bit.bitX == maxBit ||
            bit.bitY == 0 || bit.bitY == maxBit ||
            bit.bitZ == 0 || bit.bitZ == maxBit) {
            return true;
        }

        // Either one of each of the three opposing sides need to be exposed
        if ((carvedBits.testBit(bit.getBitToDirection(ForgeDirection.WEST))
            || carvedBits.testBit(bit.getBitToDirection(ForgeDirection.EAST)))
            && (carvedBits.testBit(bit.getBitToDirection(ForgeDirection.SOUTH))
            || carvedBits.testBit(bit.getBitToDirection(ForgeDirection.NORTH)))
            && (carvedBits.testBit(bit.getBitToDirection(ForgeDirection.UP))
            || carvedBits.testBit(bit.getBitToDirection(ForgeDirection.DOWN)))) {
            return true;
        }

        // Surrounding bits need to be exposed
        // to any one side the carved bit is exposed
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            CarvingBit sideBit = bit.getBitToDirection(d);
            if (carvedBits.testBit(sideBit)) {
                // Each of the four bits around the side bit that had been carved
                // need to have been carved out too
                int sideBitNeighborCarvedCount = 0;
                for (ForgeDirection ds : ForgeDirection.VALID_DIRECTIONS) {
                    // Skip back and front relative to the bit being carved out
                    if (ds != d && ds != d.getOpposite()) {
                        CarvingBit sideBitNeighbor = sideBit.getBitToDirection(ds);
                        if (carvedBits.testBit(sideBitNeighbor)) {
                            sideBitNeighborCarvedCount++;
                        }
                    }
                }

                if (sideBitNeighborCarvedCount == 4) {
                    return true;
                } else {
                    Bids.LOG.debug("Not enough exposed bits around to " + d + ": " + sideBitNeighborCarvedCount);
                }
            }
        }

        Bids.LOG.debug("Cannot carve bit that is too deep");

        return false;
    }

    @Override
    public List<CarvingBit> getBitsToCarve(CarvingBit selectedBit, int side, CarvingBitMap currentCarvedBits) {
        return Collections.singletonList(selectedBit);
    }

    @Override
    public AxisAlignedBB getSelectedBitBounds(CarvingBit selected, int side) {
        float div = 1f / TileEntityCarving.CARVING_DIMENSION;

        double minX = selected.bitX * div;
        double maxX = minX + div;
        double minY = selected.bitY * div;
        double maxY = minY + div;
        double minZ = selected.bitZ * div;
        double maxZ = minZ + div;

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

}
