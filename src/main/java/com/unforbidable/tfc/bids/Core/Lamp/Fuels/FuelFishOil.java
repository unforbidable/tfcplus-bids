package com.unforbidable.tfc.bids.Core.Lamp.Fuels;

import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Interfaces.ILampFuelMaterial;
import net.minecraftforge.fluids.FluidStack;

public class FuelFishOil implements ILampFuelMaterial {

    @Override
    public boolean isFuelValid(FluidStack fluidStack) {
        return true;
    }

    @Override
    public float getFuelConsumptionRate(FluidStack fluidStack) {
        return BidsOptions.LightSources.clayLampFishOilConsumption;
    }

    @Override
    public float getFuelLightLevel(FluidStack fluidStack) {
        return BidsOptions.LightSources.clayLampFishOilLightLevel;
    }

}
