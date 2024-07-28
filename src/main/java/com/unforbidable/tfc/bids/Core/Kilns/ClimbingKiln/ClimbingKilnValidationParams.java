package com.unforbidable.tfc.bids.Core.Kilns.ClimbingKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import net.minecraftforge.common.util.ForgeDirection;

public class ClimbingKilnValidationParams extends KilnValidationParams {

    public final ForgeDirection direction;
    public final int height;

    public ClimbingKilnValidationParams(ForgeDirection direction, int height) {
        this.direction = direction;
        this.height = height;
    }

    @Override
    public String toString() {
        return "{" +
            "direction=" + direction +
            ", height=" + height +
            '}';
    }

}
