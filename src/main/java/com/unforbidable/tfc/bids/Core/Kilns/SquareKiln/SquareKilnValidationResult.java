package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidatorResult;
import net.minecraftforge.common.util.ForgeDirection;

public class SquareKilnValidationResult extends KilnValidatorResult {

    // Tunnel kiln is validated in a certain horizontal direction
    // with chimney being rotated LEFT or RIGHT
    public final ForgeDirection direction;
    public final SquareKilnChimneyRotation chimneyRotation;

    public SquareKilnValidationResult(boolean valid, ForgeDirection direction, SquareKilnChimneyRotation chimneyRotation) {
        super(valid);

        this.direction = direction;
        this.chimneyRotation = chimneyRotation;
    }

    public static SquareKilnValidationResult success(ForgeDirection direction, SquareKilnChimneyRotation offset) {
        return new SquareKilnValidationResult(true, direction, offset);
    }

    public static SquareKilnValidationResult failure() {
        return new SquareKilnValidationResult(false, ForgeDirection.UNKNOWN, SquareKilnChimneyRotation.UNKNOWN);
    }

}
