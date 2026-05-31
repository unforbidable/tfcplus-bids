package com.unforbidable.tfc.bids.core.features.init.fluid;

import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import net.minecraftforge.fluids.Fluid;
import java.util.function.Function;

public class FluidSpec<T extends Fluid> {

    public final String name;
    public final Function<String, T> constructor;
    public final int color;

    public FluidSpec(String name, Function<String, T> constructor, int color) {
        this.name = name;
        this.constructor = constructor;
        this.color = color;
    }

    public T getInstance() {
        T instance = constructor.apply(name);

        if (instance instanceof FluidCommon) {
            ((FluidCommon) instance).setBaseColor(color);
        }

        return instance;
    }

}
