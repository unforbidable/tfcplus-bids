package com.unforbidable.tfc.bids.core.features.init.fluid;

import net.minecraftforge.fluids.Fluid;
import java.util.function.Function;

public class FluidSpecBuilder<T extends Fluid> {

    private final String name;
    private final Function<String, T> constructor;
    private int color = 0xffffff;

    public FluidSpecBuilder(String name, Function<String, T> constructor) {
        this.name = name;
        this.constructor = constructor;
    }

    public FluidSpecBuilder<T> color(int color) {
        this.color = color;

        return this;
    }

    public FluidSpec<T> build() {
        return new FluidSpec<>(name, constructor, color);
    }

}
