package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HandworkManager {

    static final List<HandworkRecipe> recipes = new ArrayList<HandworkRecipe>();

    public static void addRecipe(HandworkRecipe recipe) {
        recipes.add(recipe);
    }

    public static HandworkRecipe getMatchingRecipe(ItemStack ingredient) {
        for (HandworkRecipe recipe : recipes) {
            if (recipe.matches(ingredient)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<HandworkRecipe> getRecipes() {
        return new ArrayList<HandworkRecipe>(recipes);
    }

}
