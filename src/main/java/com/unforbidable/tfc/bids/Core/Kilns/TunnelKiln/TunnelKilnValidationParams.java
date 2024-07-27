package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import net.minecraftforge.common.util.ForgeDirection;

public class TunnelKilnValidationParams extends KilnValidationParams {

    public final ForgeDirection direction;
    public final int height;

    public TunnelKilnValidationParams(ForgeDirection direction, int height) {
        this.direction = direction;
        this.height = height;
    }

}
