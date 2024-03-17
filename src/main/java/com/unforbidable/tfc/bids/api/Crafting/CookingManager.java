package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookingManager {

    private final static List<CookingRecipe> recipes = new ArrayList<CookingRecipe>();
    private final static Map<String, CookingMixture> cookingMixtures = new HashMap<String, CookingMixture>();
    private final static Map<Item, Item> cookingIngredientOverrides = new HashMap<Item, Item>();

    public static void addRecipe(CookingRecipe recipe) {
        recipes.add(recipe);
    }

    public static List<CookingRecipe> getRecipes() {
        return new ArrayList<CookingRecipe>(recipes);
    }

    public static List<CookingRecipe> getRecipesMatchingInput(ItemStack inputItemStack) {
        List<CookingRecipe> matches = new ArrayList<CookingRecipe>();

        for (CookingRecipe recipe : recipes) {
            if (recipe.matchesInput(inputItemStack)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static List<CookingRecipe> getRecipesMatchingInput(FluidStack inputFluidStack) {
        List<CookingRecipe> matches = new ArrayList<CookingRecipe>();

        for (CookingRecipe recipe : recipes) {
            if (recipe.matchesInput(inputFluidStack)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static List<CookingRecipe> getRecipesMatchingOutput(FluidStack outputFluidStack) {
        List<CookingRecipe> matches = new ArrayList<CookingRecipe>();

        for (CookingRecipe recipe : recipes) {
            if (recipe.matchesOutput(outputFluidStack)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static List<CookingRecipe> getRecipesMatchingOutput(ItemStack outputItemStack) {
        List<CookingRecipe> matches = new ArrayList<CookingRecipe>();

        for (CookingRecipe recipe : recipes) {
            if (recipe.matchesOutput(outputItemStack)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static List<CookingRecipe> getRecipesMatchingTemplate(CookingRecipe template) {
        List<CookingRecipe> matches = new ArrayList<CookingRecipe>();

        for (CookingRecipe recipe : recipes) {
            if (recipe.matchesTemplate(template)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static void registerCookingMixture(CookingMixture cookingMixture) {
        cookingMixtures.put(cookingMixture.getName(), cookingMixture);
    }

    public static CookingMixture getCookingMixture(String name) {
        return cookingMixtures.get(name);
    }

    public static void registerCookingIngredientOverride(Item original, Item override) {
        cookingIngredientOverrides.put(original, override);
    }

    public static Item getCookingIngredientOverride(Item item) {
        return cookingIngredientOverrides.get(item);
    }

}
