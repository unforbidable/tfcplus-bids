package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WoodworkingManager {

    static final List<WoodworkingRecipe> recipes = new ArrayList<WoodworkingRecipe>();

    public static void addRecipe(WoodworkingRecipe recipe) {
        recipes.add(recipe);
    }

    public static List<WoodworkingRecipe> findMatchingRecipes(ItemStack itemStack) {
        List<WoodworkingRecipe> matching = new ArrayList<WoodworkingRecipe>();

        for (WoodworkingRecipe recipe : recipes) {
            if (recipe.matches(itemStack)) {
                matching.add(recipe);
            }
        }

        return matching;
    }

    public static List<WoodworkingRecipe> getRecipes() {
        return new ArrayList<WoodworkingRecipe>(recipes);
    }

}
