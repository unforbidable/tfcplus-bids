package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.world.World;

public class TunnelKilnChamber extends KilnChamber<TunnelKilnValidator, TunnelKilnValidationParams> {

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
