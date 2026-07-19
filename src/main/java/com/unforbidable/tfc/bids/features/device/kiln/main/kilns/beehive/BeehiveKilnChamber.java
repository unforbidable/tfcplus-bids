package com.unforbidable.tfc.bids.features.device.kiln.main.kilns.beehive;

import com.unforbidable.tfc.bids.api.features.kiln.KilnHeatSource;
import com.unforbidable.tfc.bids.features.device.kiln.main.KilnValidationParams;
import com.unforbidable.tfc.bids.features.device.kiln.main.ValidatorKilnChamber;
import net.minecraft.world.World;

public class BeehiveKilnChamber extends ValidatorKilnChamber<BeehiveKilnValidator, KilnValidationParams> {

    public BeehiveKilnChamber(KilnHeatSource heatSource) {
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
