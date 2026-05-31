package com.unforbidable.tfc.bids.core.features.setup.fluidcontainer;

import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;

public class FluidContainerSpec {

    public final Fluid fluid;
    public final Item filled;
    public final int filledDamage;
    public final int volume;
    public final boolean partial;
    public final Item empty;
    public final int emptyDamage;

    public FluidContainerSpec(Fluid fluid, Item filled, int filledDamage, int volume, boolean partial, Item empty, int emptyDamage) {
        this.fluid = fluid;
        this.filled = filled;
        this.filledDamage = filledDamage;
        this.volume = volume;
        this.partial = partial;
        this.empty = empty;
        this.emptyDamage = emptyDamage;
    }

}
