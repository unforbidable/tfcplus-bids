package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StonePressManager {

    static final List<StonePressRecipe> recipes = new ArrayList<StonePressRecipe>();

    public static void addRecipe(StonePressRecipe recipe) {
        recipes.add(recipe);
    }

    public static StonePressRecipe getMatchingRecipe(ItemStack itemStack) {
        for (StonePressRecipe recipe : recipes) {
            if (recipe.matches(itemStack)) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean hasMatchingRecipe(ItemStack itemStack) {
        return getMatchingRecipe(itemStack) != null;
    }

    public static List<StonePressRecipe> getRecipes() {
        return new ArrayList<StonePressRecipe>(recipes);
    }

}
