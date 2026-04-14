package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DryingSurfaceManager {

    private static final List<DryingSurfaceRecipe> recipes = new ArrayList<DryingSurfaceRecipe>();

    public static void addRecipe(DryingSurfaceRecipe recipe) {
        recipes.add(recipe);
    }

    public static DryingSurfaceRecipe getMatchingRecipe(ItemStack input) {
        for (DryingSurfaceRecipe recipe : recipes) {
            if (recipe.matches(input)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<DryingSurfaceRecipe> getRecipes() {
        return new ArrayList<DryingSurfaceRecipe>(recipes);
    }

}
