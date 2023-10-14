package com.unforbidable.tfc.bids.Core.Lamp;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.Interfaces.ILampFuelMaterial;
import com.unforbidable.tfc.bids.api.LampRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LampHelper {

    public static boolean isValidLampFuel(FluidStack fluidStack) {
        ILampFuelMaterial fuel = LampRegistry.findFuel(fluidStack.getFluid());
        return fuel != null && fuel.isFuelValid(fluidStack);
    }

    public static float getFuelConsumptionRate(FluidStack fluidStack) {
        ILampFuelMaterial fuel = LampRegistry.findFuel(fluidStack.getFluid());
        return fuel != null ? fuel.getFuelConsumptionRate(fluidStack) : 0f;
    }

    public static float getFuelLightLevel(FluidStack fluidStack) {
        ILampFuelMaterial fuel = LampRegistry.findFuel(fluidStack.getFluid());
        return fuel != null ? fuel.getFuelLightLevel(fluidStack) : 0f;
    }

}
