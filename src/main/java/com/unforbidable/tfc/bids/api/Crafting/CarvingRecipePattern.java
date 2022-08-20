package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;

public class CarvingRecipePattern {

    final int dimension;
    final CarvingBitMap carvedPattern;

    int currentLayer = 3;

    public CarvingRecipePattern() {
        this(TileEntityCarving.CARVING_DIMENSION);
    }

    public CarvingRecipePattern(int dimension) {
        this.dimension = dimension;
        carvedPattern = new CarvingBitMap(dimension);
    }

    public CarvingRecipePattern carveEntireLayer() {
        if (canCarveLayer()) {
            for (int bitX = 0; bitX < dimension; bitX++) {
                for (int bitZ = 0; bitZ < dimension; bitZ++) {
                    carvedPattern.setBit(new CarvingBit(bitX, currentLayer, bitZ));
                }
            }

            currentLayer--;
        }

        return this;
    }

    private boolean canCarveLayer() {
        return currentLayer >= 0;
    }

    public boolean matchCarving(CarvingBitMap carvedBits) {
        for (int bitX = 0; bitX < dimension; bitX++) {
            for (int bitY = 0; bitY < dimension; bitY++) {
                for (int bitZ = 0; bitZ < dimension; bitZ++) {
                    if (carvedPattern.testBit(bitX, bitY, bitZ) != carvedBits.testBit(bitX, bitY, bitZ)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean testBit(int x, int y, int z) {
        return carvedPattern.testBit(x, y, z);
    }

}
