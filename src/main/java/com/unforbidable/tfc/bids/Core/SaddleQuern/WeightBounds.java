package com.unforbidable.tfc.bids.Core.SaddleQuern;

import net.minecraft.util.AxisAlignedBB;

public class WeightBounds {

    AxisAlignedBB total;
    AxisAlignedBB weightGrounded;
    AxisAlignedBB weightLifted;

    public static WeightBounds get() {
        WeightBounds bounds = new WeightBounds();

        bounds.total = AxisAlignedBB.getBoundingBox(0.125f, 0, 0.125f, 0.875f, 1, 0.875f);
        bounds.weightGrounded = AxisAlignedBB.getBoundingBox(0.125f, 0, 0.125f, 0.875f, 0.75f, 0.875f);
        bounds.weightLifted = AxisAlignedBB.getBoundingBox(0.125f, 0.25f, 0.125f, 0.875f, 1, 0.875f);

        return bounds;
    }

    public AxisAlignedBB getTotal() {
        return total;
    }

    public AxisAlignedBB getWeightGrounded() {
        return weightGrounded;
    }

    public AxisAlignedBB getWeightLifted() {
        return weightLifted;
    }
}
