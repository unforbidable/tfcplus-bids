package com.unforbidable.tfc.bids.features.device.kiln.main.kilns.tunnel;

import com.unforbidable.tfc.bids.features.device.kiln.main.KilnValidationParams;
import net.minecraftforge.common.util.ForgeDirection;

public class TunnelKilnValidationParams extends KilnValidationParams {

    public final ForgeDirection direction;
    public final int height;

    public TunnelKilnValidationParams(ForgeDirection direction, int height) {
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
