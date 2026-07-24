package com.unforbidable.tfc.bids.core.features.init.item;

import net.minecraftforge.fluids.Fluid;

public class FluidSpec {

    public final int volume;
    public final Fluid fluid;
    public final boolean partial;

    public FluidSpec(int volume, Fluid fluid, boolean partial) {
        this.volume = volume;
        this.fluid = fluid;
        this.partial = partial;
    }

}
