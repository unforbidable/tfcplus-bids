package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface CookingHost {

    CookingRecipeInputTemplate createRecipeTemplate();

    CookingHeatLevel getHeatLevel();

    FluidStack getFluidStack();

    ItemStack getInputItemStack();

    int getMaxFluidVolume();

    int getTotalLiquidVolume();

    void onItemChanged(ItemStack output);

    void onFluidChanged(FluidStack output);

    boolean isValidCookingRecipe(CookingRecipe recipe);

    boolean isValidFluid(Fluid fluid);

}
