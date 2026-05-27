package com.unforbidable.tfc.bids.features.device.lamp.main;

import com.unforbidable.tfc.bids.api.features.lamp.LampFuelMaterial;
import com.unforbidable.tfc.bids.features.device.lamp.LampRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LampHelper {

    public static boolean isValidLampFuel(FluidStack fluidStack) {
        LampFuelMaterial fuel = LampRegistry.fuel.get(fluidStack.getFluid());
        return fuel != null && fuel.isFuelValid(fluidStack);
    }

    public static float getFuelConsumptionRate(FluidStack fluidStack) {
        LampFuelMaterial fuel = LampRegistry.fuel.get(fluidStack.getFluid());
        return fuel != null ? fuel.getFuelConsumptionRate(fluidStack) : 0f;
    }

    public static float getFuelLightLevel(FluidStack fluidStack) {
        LampFuelMaterial fuel = LampRegistry.fuel.get(fluidStack.getFluid());
        return fuel != null ? fuel.getFuelLightLevel(fluidStack) : 0f;
    }

}
