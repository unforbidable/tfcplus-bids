package com.unforbidable.tfc.bids.Core.SaddleQuern;

import net.minecraft.util.AxisAlignedBB;

public class LeverBounds {

    private AxisAlignedBB logBounds;
    private AxisAlignedBB pivotBounds;

    private AxisAlignedBB[] ropes;

    static final float ropeSize = 1 / 16f;

    public static LeverBounds fromOrientation(int orientation) {
        int index = orientation % 2;

        LeverBounds bounds = new LeverBounds();

        if (orientation % 2 == 0) {
            bounds.logBounds = AxisAlignedBB.getBoundingBox(0.375, 0.25, 0, 0.625, 0.5, 1);
        } else {
            bounds.logBounds = AxisAlignedBB.getBoundingBox(0, 0.25, 0.375, 1, 0.5, 0.625);
        }

        if (orientation % 2 == 0) {
            bounds.pivotBounds = AxisAlignedBB.getBoundingBox(0.25, 0, 0.375, 0.75, 0.25, 0.625);
        } else {
            bounds.pivotBounds = AxisAlignedBB.getBoundingBox(0.375, 0, 0.25, 0.625, 0.25, 0.75);
        }

        bounds.ropes = new AxisAlignedBB[3];
        if (orientation % 2 == 0) {
            bounds.ropes[0] = AxisAlignedBB.getBoundingBox(0.375 - ropeSize, 0.25 - ropeSize, 0.5 - ropeSize * 1.5, 0.625 + ropeSize, 0.5 + ropeSize, 0.5 + ropeSize * 1.5);
            bounds.ropes[1] = AxisAlignedBB.getBoundingBox(0.375 - ropeSize, 0, 0.5 - ropeSize * 0.5, 0.375, 0.5 - ropeSize, 0.5 + ropeSize * 0.5);
            bounds.ropes[2] = AxisAlignedBB.getBoundingBox(0.625, 0, 0.5 - ropeSize * 0.5, 0.625 + ropeSize, 0.5 - ropeSize, 0.5 + ropeSize * 0.5);
        } else {
            bounds.ropes[0] = AxisAlignedBB.getBoundingBox(0.5 - ropeSize * 1.5, 0.25 - ropeSize, 0.375 - ropeSize, 0.5 + ropeSize * 1.5, 0.5 + ropeSize, 0.625 + ropeSize);
            bounds.ropes[1] = AxisAlignedBB.getBoundingBox(0.5 - ropeSize * 0.5, 0, 0.375 - ropeSize, 0.5 + ropeSize * 0.5, 0.5 - ropeSize, 0.375);
            bounds.ropes[2] = AxisAlignedBB.getBoundingBox(0.5 - ropeSize * 0.5, 0, 0.625, 0.5 + ropeSize * 0.5, 0.5 - ropeSize, 0.625 + ropeSize);
        }

        return bounds;
    }

    public AxisAlignedBB getLog() {
        return logBounds;
    }

    public AxisAlignedBB getPivot() {
        return pivotBounds;
    }

    public AxisAlignedBB[] getRopes() {
        return ropes;
    }
}
