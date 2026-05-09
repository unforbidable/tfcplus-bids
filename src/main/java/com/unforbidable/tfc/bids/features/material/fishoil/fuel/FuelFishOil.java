package com.unforbidable.tfc.bids.features.material.fishoil.fuel;

import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.ILampFuelMaterial;
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
