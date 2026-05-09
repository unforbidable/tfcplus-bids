package com.unforbidable.tfc.bids.api._obsolete.Registry;

import net.minecraftforge.fluids.Fluid;

public class FluidRegistry<T> extends MapRegistry<Fluid, T> {

    @Override
    protected String getName(Fluid key) {
        return key.getUnlocalizedName();
    }

}
