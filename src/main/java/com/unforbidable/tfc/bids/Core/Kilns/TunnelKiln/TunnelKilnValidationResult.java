package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidatorResult;
import net.minecraftforge.common.util.ForgeDirection;

public class TunnelKilnValidationResult extends KilnValidatorResult {

    // Tunnel kiln is validated in a certain horizontal direction
    public final ForgeDirection direction;

    public TunnelKilnValidationResult(boolean valid, ForgeDirection direction) {
        super(valid);

        this.direction = direction;
    }

    public static TunnelKilnValidationResult success(ForgeDirection direction) {
        return new TunnelKilnValidationResult(true, direction);
    }

    public static TunnelKilnValidationResult failure() {
        return new TunnelKilnValidationResult(false, ForgeDirection.UNKNOWN);
    }

}
