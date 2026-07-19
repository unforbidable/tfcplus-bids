package com.unforbidable.tfc.bids.features.device.kiln.main.kilns.square;

import com.unforbidable.tfc.bids.api.features.kiln.KilnHeatSource;
import com.unforbidable.tfc.bids.features.device.kiln.main.ValidatorKilnChamber;
import net.minecraft.world.World;

public class SquareKilnChamber extends ValidatorKilnChamber<SquareKilnValidator, SquareKilnValidationParams> {

    public SquareKilnChamber(KilnHeatSource heatSource) {
        super(heatSource);
    }

    @Override
    protected SquareKilnValidator createValidator(World world, int x, int y, int z) {
        return new SquareKilnValidator(world, x, y, z);
    }

    @Override
    public String getName() {
        return "SQUARE_KILN";
    }

}
