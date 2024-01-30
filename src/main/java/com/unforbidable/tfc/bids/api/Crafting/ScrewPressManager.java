package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScrewPressManager {

    static final List<ScrewPressRecipe> recipes = new ArrayList<ScrewPressRecipe>();

    public static void addRecipe(ScrewPressRecipe recipe) {
        recipes.add(recipe);
    }

    public static ScrewPressRecipe getMatchingRecipe(ItemStack itemStack) {
        for (ScrewPressRecipe recipe : recipes) {
            if (recipe.matches(itemStack)) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean hasMatchingRecipe(ItemStack itemStack) {
        return getMatchingRecipe(itemStack) != null;
    }

    public static List<ScrewPressRecipe> getRecipes() {
        return new ArrayList<ScrewPressRecipe>(recipes);
    }

}
