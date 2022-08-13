package com.unforbidable.tfc.bids.api.Crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class DryingManager {

    static final List<DryingRecipe> recipes = new ArrayList<DryingRecipe>();

    public static void addRecipe(DryingRecipe recipe) {
        recipes.add(recipe);
    }

    public static DryingRecipe getMatchingRecipe(ItemStack itemStack) {
        for (DryingRecipe recipe : recipes) {
            if (recipe.matches(itemStack)) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean hasMatchingRecipe(ItemStack itemStack) {
        return getMatchingRecipe(itemStack) != null;
    }

    public static List<DryingRecipe> getRecipes() {
        return new ArrayList<DryingRecipe>(recipes);
    }

}
