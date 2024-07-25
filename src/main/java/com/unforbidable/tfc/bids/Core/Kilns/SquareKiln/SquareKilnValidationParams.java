package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import net.minecraftforge.common.util.ForgeDirection;

public class SquareKilnValidationParams extends KilnValidationParams {

    public final ForgeDirection direction;
    public final SquareKilnChimneyRotation chimneyRotation;
    public final int height;

    public SquareKilnValidationParams(ForgeDirection direction, SquareKilnChimneyRotation chimneyRotation, int height) {
        this.direction = direction;
        this.chimneyRotation = chimneyRotation;
        this.height = height;
    }

}
