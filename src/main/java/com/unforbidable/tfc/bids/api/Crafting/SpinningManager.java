package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpinningManager {

    static final List<SpinningRecipe> recipes = new ArrayList<SpinningRecipe>();

    public static void addRecipe(SpinningRecipe recipe) {
        recipes.add(recipe);
    }

    public static SpinningRecipe getMatchingRecipe(ItemStack ingredient) {
        for (SpinningRecipe recipe : recipes) {
            if (recipe.matches(ingredient)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<SpinningRecipe> getRecipes() {
        return new ArrayList<SpinningRecipe>(recipes);
    }

}
