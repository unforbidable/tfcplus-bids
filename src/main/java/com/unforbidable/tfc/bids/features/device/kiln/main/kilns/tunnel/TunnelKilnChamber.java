package com.unforbidable.tfc.bids.features.device.kiln.main.kilns.tunnel;

import com.unforbidable.tfc.bids.api.features.kiln.KilnHeatSource;
import com.unforbidable.tfc.bids.features.device.kiln.main.ValidatorKilnChamber;
import net.minecraft.world.World;

public class TunnelKilnChamber extends ValidatorKilnChamber<TunnelKilnValidator, TunnelKilnValidationParams> {

    public TunnelKilnChamber(KilnHeatSource heatSource) {
        super(heatSource);
    }

    @Override
    protected TunnelKilnValidator createValidator(World world, int x, int y, int z) {
        return new TunnelKilnValidator(world, x, y, z);
    }

    @Override
    public String getName() {
        return "TUNNEL_KILN";
    }

}
