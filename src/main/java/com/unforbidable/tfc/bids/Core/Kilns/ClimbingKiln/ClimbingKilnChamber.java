package com.unforbidable.tfc.bids.Core.Kilns.ClimbingKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.world.World;

public class ClimbingKilnChamber extends KilnChamber<ClimbingKilnValidator, ClimbingKilnValidationParams> {

    public ClimbingKilnChamber(IKilnHeatSource heatSource) {
        super(heatSource);
    }

    @Override
    protected ClimbingKilnValidator createValidator(World world, int x, int y, int z) {
        return new ClimbingKilnValidator(world, x, y, z);
    }

    @Override
    public String getName() {
        return "CLIMBING_KILN";
    }

}
