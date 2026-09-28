package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipeCraftingResult {

    private final FluidStack outputFluidStack;
    private final ItemStack outputItemStack;

    public CookingRecipeCraftingResult(FluidStack outputFluidStack, ItemStack outputItemStack) {
        this.outputFluidStack = outputFluidStack;
        this.outputItemStack = outputItemStack;
    }

    public FluidStack getOutputFluidStack() {
        return outputFluidStack;
    }

    public ItemStack getOutputItemStack() {
        return outputItemStack;
    }

}
