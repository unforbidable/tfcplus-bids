package com.unforbidable.tfc.bids.api.features.cooking;

import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeCraftingResult;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface CookingHost {

    CookingRecipeInputTemplate createRecipeTemplate();

    CookingHeatLevel getHeatLevel();

    FluidStack getFluidStack();

    ItemStack getInputItemStack();

    int getMaxFluidVolume();

    int getTotalLiquidVolume();

    void onItemChanged(ItemStack output);

    void onFluidChanged(FluidStack output, FluidStack secondaryOutput);

}
