package com.unforbidable.tfc.bids.Core.SaddleQuern;

import net.minecraft.util.AxisAlignedBB;

public class LeverBounds {

    private AxisAlignedBB logBounds;

    public static LeverBounds fromOrientation(int orientation) {
        int index = orientation % 2;

        LeverBounds bounds = new LeverBounds();

        if (orientation % 2 == 0) {
            bounds.logBounds = AxisAlignedBB.getBoundingBox(0.375, 0, 0, 0.625, 0.25, 1);
        } else {
            bounds.logBounds = AxisAlignedBB.getBoundingBox(0, 0, 0.375, 1, 0.25, 0.625);
        }

        return bounds;
    }

    public AxisAlignedBB getLogBounds() {
        return logBounds;
    }
}
