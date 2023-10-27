package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PrepManager {

    static final List<PrepRecipe> recipes = new ArrayList<PrepRecipe>();

    public static void addRecipe(PrepRecipe recipe) {
        recipes.add(recipe);
    }

    public static PrepRecipe getMatchingRecipe(ItemStack[] ingredients) {
        for (PrepRecipe recipe : recipes) {
            if (recipe.matches(ingredients)) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean isValidPrepVessel(ItemStack is) {
        for (PrepRecipe recipe : recipes) {
            if (recipe.doesVesselMatch(is)) {
                return true;
            }
        }

        return false;
    }

    public static List<PrepRecipe> getRecipesUsingVessel(ItemStack is) {
        List<PrepRecipe> list = new ArrayList<PrepRecipe>();

        for (PrepRecipe recipe : recipes) {
            if (recipe.doesVesselMatch(is)) {
                list.add(recipe);
            }
        }

        return list;
    }

    public boolean hasMatchingRecipe(ItemStack[] ingredients) {
        return getMatchingRecipe(ingredients) != null;
    }

    public static List<PrepRecipe> getRecipes() {
        return recipes;
    }

}
