package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import net.minecraftforge.common.util.ForgeDirection;

public class TunnelKilnValidatorParams extends KilnValidationParams {

    public final ForgeDirection direction;
    public final int height;

    public TunnelKilnValidatorParams(ForgeDirection direction, int height) {
        this.direction = direction;
        this.height = height;
    }

}
