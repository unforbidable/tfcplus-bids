package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.world.World;

public class SquareKilnChamber extends KilnChamber<SquareKilnValidator, SquareKilnValidationParams> {

    public SquareKilnChamber(IKilnHeatSource heatSource) {
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
