package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Crafting.Builders.CookingCheeseRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipeCraftingResult {

    private final FluidStack outputFluidStack;
    private final FluidStack secondaryOutputFluidStack;
    private final ItemStack outputItemStack;

    public CookingRecipeCraftingResult(FluidStack outputFluidStack, FluidStack secondaryOutputFluidStack, ItemStack outputItemStack) {
        this.outputFluidStack = outputFluidStack;
        this.secondaryOutputFluidStack = secondaryOutputFluidStack;
        this.outputItemStack = outputItemStack;
    }

    public FluidStack getOutputFluidStack() {
        return outputFluidStack;
    }

    public FluidStack getSecondaryOutputFluidStack() {
        return secondaryOutputFluidStack;
    }

    public ItemStack getOutputItemStack() {
        return outputItemStack;
    }

}
