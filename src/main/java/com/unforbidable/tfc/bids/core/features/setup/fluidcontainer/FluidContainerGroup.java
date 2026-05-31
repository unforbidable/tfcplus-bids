package com.unforbidable.tfc.bids.core.features.setup.fluidcontainer;

import net.minecraftforge.fluids.Fluid;
import java.util.List;

public class FluidContainerGroup {

    public final Fluid fluid;
    public final List<FluidContainerSpec> containers;

    public FluidContainerGroup(Fluid fluid, List<FluidContainerSpec> containers) {
        this.fluid = fluid;
        this.containers = containers;
    }

}
