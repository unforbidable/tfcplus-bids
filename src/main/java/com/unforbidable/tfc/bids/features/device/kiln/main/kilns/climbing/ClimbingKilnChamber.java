package com.unforbidable.tfc.bids.features.device.kiln.main.kilns.climbing;

import com.unforbidable.tfc.bids.features.device.kiln.main.ValidatorKilnChamber;
import com.unforbidable.tfc.bids.api.features.kiln.KilnHeatSource;
import net.minecraft.world.World;

public class ClimbingKilnChamber extends ValidatorKilnChamber<ClimbingKilnValidator, ClimbingKilnValidationParams> {

    public ClimbingKilnChamber(KilnHeatSource heatSource) {
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
