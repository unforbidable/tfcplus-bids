package com.unforbidable.tfc.bids.Core.SaddleQuern;

import net.minecraft.util.AxisAlignedBB;

public class LeverBounds {

    private AxisAlignedBB logBounds;
    private AxisAlignedBB pivotBounds;

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

        return bounds;
    }

    public AxisAlignedBB getLogBounds() {
        return logBounds;
    }

    public AxisAlignedBB getPivotBounds() {
        return pivotBounds;
    }
}
