package com.unforbidable.tfc.bids.core.features.setup.fluidcontainer;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;

public class FluidContainerGroupBuilder {

    private final Fluid fluid;
    private final List<FluidContainerSpec> containers = new ArrayList<>();

    public FluidContainerGroupBuilder(Fluid fluid) {
        this.fluid = fluid;
    }

    public FluidContainerGroupBuilder container(Item full, int volume, boolean partial, Item empty) {
        return container(full, 0, volume, partial, empty, 0);
    }

    public FluidContainerGroupBuilder container(Item full, int fullDamage, int volume, boolean partial, Item empty) {
        return container(full, fullDamage, volume, partial, empty, 0);
    }

    public FluidContainerGroupBuilder container(Item full, int volume, boolean partial, Item empty, int emptyDamage) {
        return container(full, 0, volume, partial, empty, emptyDamage);
    }

    public FluidContainerGroupBuilder container(Item full, int fullDamage, int volume, boolean partial, Item empty, int emptyDamage) {
        containers.add(new FluidContainerSpec(fluid, full, fullDamage, volume, partial, empty, emptyDamage));

        return this;
    }

    public FluidContainerGroup build() {
        return new FluidContainerGroup(fluid, containers);
    }

}
