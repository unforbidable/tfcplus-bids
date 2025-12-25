package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RopeMakingManager {

    static final List<RopeMakingRecipe> recipes = new ArrayList<RopeMakingRecipe>();

    public static void addRecipe(RopeMakingRecipe recipe) {
        recipes.add(recipe);
    }

    public static RopeMakingRecipe getMatchingRecipe(ItemStack ingredient) {
        for (RopeMakingRecipe recipe : recipes) {
            if (recipe.matches(ingredient)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<RopeMakingRecipe> getRecipes() {
        return new ArrayList<RopeMakingRecipe>(recipes);
    }

}
