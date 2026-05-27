package com.unforbidable.tfc.bids.api.features.lamp;

import net.minecraftforge.fluids.FluidStack;

public interface LampFuelMaterial {

    boolean isFuelValid(FluidStack fluidStack);

    float getFuelConsumptionRate(FluidStack fluidStack);

    float getFuelLightLevel(FluidStack fluidStack);

}
