package com.unforbidable.tfc.bids.Core.Kilns.BeehiveKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnChamber;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.world.World;

public class BeehiveKilnChamber extends KilnChamber<BeehiveKilnValidator, KilnValidationParams> {

    public BeehiveKilnChamber(IKilnHeatSource heatSource) {
        super(heatSource);
    }

    @Override
    protected BeehiveKilnValidator createValidator(World world, int x, int y, int z) {
        return new BeehiveKilnValidator(world, x, y, z);
    }

    @Override
    public String getName() {
        return "BEEHIVE_KILN";
    }

}
