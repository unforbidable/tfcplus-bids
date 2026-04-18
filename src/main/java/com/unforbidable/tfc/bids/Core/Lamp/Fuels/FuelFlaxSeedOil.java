package com.unforbidable.tfc.bids.Core.Lamp.Fuels;

import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Interfaces.ILampFuelMaterial;
import net.minecraftforge.fluids.FluidStack;

public class FuelFlaxSeedOil implements ILampFuelMaterial {

    @Override
    public boolean isFuelValid(FluidStack fluidStack) {
        return true;
    }

    @Override
    public float getFuelConsumptionRate(FluidStack fluidStack) {
        return BidsOptions.LightSources.clayLampFlaxSeedOilConsumption;
    }

    @Override
    public float getFuelLightLevel(FluidStack fluidStack) {
        return BidsOptions.LightSources.clayLampFlaxSeedOilLightLevel;
    }

}
