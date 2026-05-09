package com.unforbidable.tfc.bids.features.device.kiln.main.kilns.tunnel;

import com.unforbidable.tfc.bids.features.device.kiln.main.ValidatorKilnChamber;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IKilnHeatSource;
import net.minecraft.world.World;

public class TunnelKilnChamber extends ValidatorKilnChamber<TunnelKilnValidator, TunnelKilnValidationParams> {

    public TunnelKilnChamber(IKilnHeatSource heatSource) {
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
