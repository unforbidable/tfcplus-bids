package com.unforbidable.tfc.bids.compat.tfc.recipe;

import com.dunk.tfc.api.Crafting.BarrelRecipe;
import com.unforbidable.tfc.bids.BidsEventFactory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeTrackingHelper {

    public static void onBarrelRecipeItemCrafted(ItemStack result, BarrelRecipe recipe, ItemStack input, FluidStack fluid) {
        if (recipe.recipeIS != null && recipe.recipeFluid != null && recipe.recipeOutIS != null && recipe.recipeOutFluid != null) {
            // Looks like a soaking recipe
            BidsEventFactory.onSoakingItemCrafted(input, result, fluid);
        }
    }

}
