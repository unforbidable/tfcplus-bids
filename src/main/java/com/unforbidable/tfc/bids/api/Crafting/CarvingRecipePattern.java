package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;

public class CarvingRecipePattern {

    final int dimension;
    final CarvingBitMap carvedPattern;

    CarvingBitMap[] rotatedCarvedPatternCache;

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

    public CarvingRecipePattern carveLayer(String... bits) {
        if (canCarveLayer()) {
            for (int bitX = 0; bitX < dimension; bitX++) {
                for (int bitZ = 0; bitZ < dimension; bitZ++) {
                    if (bits.length > bitZ && bits[bitZ].length() > bitX
                            && bits[bitZ].charAt(bitX) == '#') {
                        carvedPattern.setBit(new CarvingBit(bitX, currentLayer, bitZ));
                    }
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
        if (rotatedCarvedPatternCache == null) {
            rotatedCarvedPatternCache = new CarvingBitMap[4];

            for (int i = 0; i < 4; i++) {
                rotatedCarvedPatternCache[i] = carvedPattern.rotate(i);
            }
        }

        for (int i = 0; i < 4; i++) {
            if (carvedBits.equals(rotatedCarvedPatternCache[i])) {
                return true;
            }
        }

        return false;
    }

    public boolean testBit(int x, int y, int z) {
        return carvedPattern.testBit(x, y, z);
    }

}
