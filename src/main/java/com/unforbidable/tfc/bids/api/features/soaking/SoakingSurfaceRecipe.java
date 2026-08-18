package com.unforbidable.tfc.bids.api.features.soaking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class SoakingSurfaceRecipe {

    private final ItemStack output;
    private final ItemStack input;
    private final FluidStack fluid;

    private final int ticks;

    public SoakingSurfaceRecipe(ItemStack input, ItemStack output, FluidStack fluid, int ticks) {
        this.output = output;
        this.input = input;
        this.fluid = fluid;
        this.ticks = ticks;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public long getTicks() {
        return ticks;
    }

    public boolean matchesInput(ItemStack ingredient) {
        return input.getItem() == ingredient.getItem() && (input.getItemDamage() == ingredient.getItemDamage()
            || input.getItemDamage() == OreDictionary.WILDCARD_VALUE);
    }

    public boolean matchesFluid(Fluid fluid) {
        return this.fluid.getFluid() == fluid;
    }

    public ItemStack getResult(ItemStack ingredient) {
        return getOutput();
    }

}
