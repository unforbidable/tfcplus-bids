package com.unforbidable.tfc.bids.features.device.lamp.main.fuel;

import com.unforbidable.tfc.bids.api.features.lamp.LampFuelMaterial;
import com.unforbidable.tfc.bids.features.device.lamp.LampConfig;
import net.minecraftforge.fluids.FluidStack;

public class FuelOliveOil implements LampFuelMaterial {

    @Override
    public boolean isFuelValid(FluidStack fluidStack) {
        return true;
    }

    @Override
    public float getFuelConsumptionRate(FluidStack fluidStack) {
        return LampConfig.clayLampOliveOilConsumption;
    }

    @Override
    public float getFuelLightLevel(FluidStack fluidStack) {
        return LampConfig.clayLampOliveOilLightLevel;
    }

}
