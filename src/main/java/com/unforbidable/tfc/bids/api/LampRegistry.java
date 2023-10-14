package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Interfaces.ILampFuelMaterial;
import net.minecraftforge.fluids.Fluid;

import java.util.HashMap;
import java.util.Map;

public class LampRegistry {

    static Map<Fluid, ILampFuelMaterial> fuels = new HashMap<Fluid, ILampFuelMaterial>();

    public static void registerFuel(Fluid fluid, Class<? extends ILampFuelMaterial> implementingType) {
        ILampFuelMaterial impl;
        try {
            impl = implementingType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Default constructor for type " + implementingType.getCanonicalName()
                    + " is not available", e);
        }

        fuels.put(fluid, impl);
    }

    public static ILampFuelMaterial findFuel(Fluid fluid) {
        return fuels.get(fluid);
    }

    public static Fluid[] getFuels() {
        return fuels.keySet().toArray(new Fluid[] {});
    }

}
