package com.unforbidable.tfc.bids.api.features.soaking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SoakingRecipe {

    public final ItemStack input;
    public final ItemStack output;
    public final FluidStack fluid;
    public final boolean requiresHeat;
    public final int ticks;
    public final boolean importedFromTfc;

    public SoakingRecipe(ItemStack input, ItemStack output, FluidStack fluid) {
        this(input, output, fluid, false, 0, false);
    }

    public SoakingRecipe(ItemStack input, ItemStack output, FluidStack fluid, int ticks) {
        this(input, output, fluid, false, ticks, false);
    }

    public SoakingRecipe(ItemStack input, ItemStack output, FluidStack fluid, boolean requiresHeat) {
        this(input, output, fluid, requiresHeat, 0, false);
    }

    public SoakingRecipe(ItemStack input, ItemStack output, FluidStack fluid, boolean requiresHeat, int ticks) {
        this(input, output, fluid, requiresHeat, ticks, false);
    }

    public SoakingRecipe(ItemStack input, ItemStack output, FluidStack fluid, boolean requiresHeat, int ticks, boolean importedFromTfc) {
        this.input = input;
        this.output = output;
        this.fluid = fluid;
        this.requiresHeat = requiresHeat;
        this.ticks = ticks;
        this.importedFromTfc = importedFromTfc;
    }

}
