package com.unforbidable.tfc.bids.Core.Carving.Modes;

import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingMode;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class CarvingModeCorner implements ICarvingMode {

    @Override
    public String getName() {
        return "CORNER";
    }

    @Override
    public boolean canCarveBit(CarvingBit selectedBit, CarvingBitMap currentCarvedBits) {
        // Corners 2x2 can always be carved
        return true;
    }

    @Override
    public List<CarvingBit> getBitsToCarve(CarvingBit bit, CarvingBitMap currentCarvedBits) {
        List<CarvingBit> bitsToCarve = new ArrayList<CarvingBit>();

        int quadX = bit.bitX / 2 * 2;
        int quadY = bit.bitY / 2 * 2;
        int quadZ = bit.bitZ / 2 * 2;

        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++ ) {
                for (int z = 0; z < 2; z++) {
                    bitsToCarve.add(new CarvingBit(quadX + x, quadY + y, quadZ + z));
                }
            }
        }

        return bitsToCarve;
    }

    @Override
    public AxisAlignedBB getSelectedBitBounds(CarvingBit selected) {
        int d = TileEntityCarving.CARVING_DIMENSION;
        float div = 1f / (d * 0.5f);

        double minX = Math.floor(selected.bitX * 0.5) * div;
        double maxX = minX + div;
        double minY = Math.floor(selected.bitY * 0.5) * div;
        double maxY = minY + div;
        double minZ = Math.floor(selected.bitZ * 0.5) * div;
        double maxZ = minZ + div;

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

}
