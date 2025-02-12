package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ChurningManager {

    private static final List<ChurningRecipe> recipes = new ArrayList<ChurningRecipe>();

    public static void addRecipe(ChurningRecipe recipe) {
        recipes.add(recipe);
    }

    public static ChurningRecipe findMatchingRecipe(FluidStack fs) {
        for (ChurningRecipe recipe : recipes) {
            if (recipe.matches(fs)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<ChurningRecipe> getRecipes() {
        return recipes;
    }

}
