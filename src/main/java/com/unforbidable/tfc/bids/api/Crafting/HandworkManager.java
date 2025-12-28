package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HandworkManager {

    static final List<HandworkRecipe> recipes = new ArrayList<HandworkRecipe>();

    public static void addRecipe(HandworkRecipe recipe) {
        recipes.add(recipe);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T getMatchingRecipe(ItemStack ingredient, Class<T> type) {
        for (HandworkRecipe recipe : recipes) {
            if (recipe.getClass() == type && recipe.matches(ingredient)) {
                return (T)recipe;
            }
        }

        return null;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> List<T> getRecipes(Class<T> type) {
        ArrayList<T> typedRecipes = new ArrayList<T>();
        for (HandworkRecipe recipe : recipes) {
            if (recipe.getClass() == type) {
                typedRecipes.add((T) recipe);
            }
        }

        return typedRecipes;
    }

}
