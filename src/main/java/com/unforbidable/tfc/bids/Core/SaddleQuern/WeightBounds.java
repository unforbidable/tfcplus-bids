package com.unforbidable.tfc.bids.Core.SaddleQuern;

import net.minecraft.util.AxisAlignedBB;

public class WeightBounds {

    AxisAlignedBB weight;
    AxisAlignedBB[] ropes;

    static final float weightSize = 1 / 16f * 12;
    static final float weightMargin = 1 / 16f * 2;
    static final float ropeSize = 1 / 16f;
    static final float liftOffset = 1 / 16f * 4;

    static WeightBounds groundedCache = null;
    static WeightBounds liftedCache = null;

    private static WeightBounds getGroundedBounds() {
        WeightBounds bounds = new WeightBounds();

        bounds.weight = AxisAlignedBB.getBoundingBox(weightMargin, 0, weightMargin, 1 - weightMargin, weightSize, 1 - weightMargin);

        bounds.ropes = new AxisAlignedBB[8];

        bounds.ropes[0] = AxisAlignedBB.getBoundingBox(weightMargin - ropeSize, weightSize, 0.5 - ropeSize / 2, 1 - weightMargin + ropeSize, weightSize + ropeSize, 0.5 + ropeSize / 2);
        bounds.ropes[1] = AxisAlignedBB.getBoundingBox(0.5 - ropeSize / 2, weightSize, weightMargin - ropeSize, 0.5 + ropeSize / 2, weightSize + ropeSize, 1 - weightMargin + ropeSize);

        bounds.ropes[2] = AxisAlignedBB.getBoundingBox(weightMargin - ropeSize, 0, 0.5 - ropeSize / 2, weightMargin, weightSize, 0.5 + ropeSize / 2);
        bounds.ropes[3] = AxisAlignedBB.getBoundingBox(0.5 - ropeSize / 2, 0, weightMargin - ropeSize, 0.5 + ropeSize / 2, weightSize, weightMargin);
        bounds.ropes[4] = AxisAlignedBB.getBoundingBox(1 - weightMargin, 0, 0.5 - ropeSize / 2, 1 - weightMargin + ropeSize, weightSize, 0.5 + ropeSize / 2);
        bounds.ropes[5] = AxisAlignedBB.getBoundingBox(0.5 - ropeSize / 2, 0, 1 - weightMargin, 0.5 + ropeSize / 2, weightSize, 1 - weightMargin + ropeSize);

        bounds.ropes[6] = AxisAlignedBB.getBoundingBox(weightMargin - ropeSize, 0 - ropeSize, 0.5 - ropeSize / 2, 1 - weightMargin + ropeSize, 0, 0.5 + ropeSize / 2);
        bounds.ropes[7] = AxisAlignedBB.getBoundingBox(0.5 - ropeSize / 2, 0 - ropeSize, weightMargin - ropeSize, 0.5 + ropeSize / 2, 0, 1 - weightMargin + ropeSize);

        return bounds;
    }

    private static WeightBounds getLiftedBoundsFromGroundedCache() {
        WeightBounds bounds = new WeightBounds();

        bounds.weight = groundedCache.weight.offset(0, liftOffset, 0);

        bounds.ropes = new AxisAlignedBB[groundedCache.ropes.length];
        for (int i = 0; i < groundedCache.ropes.length; i++) {
            bounds.ropes[i] = groundedCache.ropes[i].offset(0, liftOffset, 0);
        }

        return bounds;
    }

    public static WeightBounds fromLifted(boolean lifted) {
        if (groundedCache == null) {
            groundedCache = getGroundedBounds();
        }

        if (lifted) {
            if (liftedCache == null) {
                liftedCache = getLiftedBoundsFromGroundedCache();
            }

        }

        return groundedCache;
    }

    public static AxisAlignedBB getTotal() {
        return AxisAlignedBB.getBoundingBox(weightMargin, 0, weightMargin, 1 - weightMargin, 1, 1 - weightMargin);
    }

    public AxisAlignedBB getWeight() {
        return weight;
    }

    public AxisAlignedBB[] getRopes() {
        return ropes;
    }
}
