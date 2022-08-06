package com.unforbidable.tfc.bids.api.Crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class SeasoningManager {

    static final List<SeasoningRecipe> recipes = new ArrayList<SeasoningRecipe>();

    public static void addRecipe(SeasoningRecipe recipe) {
        recipes.add(recipe);
    }

    public static SeasoningRecipe getMatchingRecipe(ItemStack itemStack) {
        for (SeasoningRecipe recipe : recipes) {
            if (recipe.matches(itemStack)) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean hasMatchingRecipe(ItemStack itemStack) {
        return getMatchingRecipe(itemStack) != null;
    }

}
