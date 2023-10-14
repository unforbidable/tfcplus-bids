package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraftforge.fluids.FluidStack;

public interface ILampFuelMaterial {

    boolean isFuelValid(FluidStack fluidStack);

    float getFuelConsumptionRate(FluidStack fluidStack);

    float getFuelLightLevel(FluidStack fluidStack);

}
