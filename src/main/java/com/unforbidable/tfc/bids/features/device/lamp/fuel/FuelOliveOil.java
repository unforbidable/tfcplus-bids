package com.unforbidable.tfc.bids.features.device.lamp.fuel;

import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.ILampFuelMaterial;
import net.minecraftforge.fluids.FluidStack;

public class FuelOliveOil implements ILampFuelMaterial {

    @Override
    public boolean isFuelValid(FluidStack fluidStack) {
        return true;
    }

    @Override
    public float getFuelConsumptionRate(FluidStack fluidStack) {
        return BidsOptions.LightSources.clayLampOliveOilConsumption;
    }

    @Override
    public float getFuelLightLevel(FluidStack fluidStack) {
        return BidsOptions.LightSources.clayLampOliveOilLightLevel;
    }

}
