package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import net.minecraftforge.common.util.ForgeDirection;

public enum SquareKilnChimneyRotation {

    LEFT(ForgeDirection.DOWN),
    RIGHT(ForgeDirection.UP),
    UNKNOWN(ForgeDirection.UNKNOWN);

    private final ForgeDirection axis;

    public static final SquareKilnChimneyRotation[] VALID_OFFSETS = new SquareKilnChimneyRotation[] { LEFT, RIGHT };

    SquareKilnChimneyRotation(ForgeDirection axis) {
        this.axis = axis;
    }

    public ForgeDirection getAxis() {
        return axis;
    }

}
