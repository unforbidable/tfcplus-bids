package com.unforbidable.tfc.bids.api.Crafting;

import java.util.ArrayList;
import java.util.List;

import com.dunk.tfc.api.Crafting.QuernManager;
import com.dunk.tfc.api.Crafting.QuernRecipe;
import com.dunk.tfc.api.Interfaces.IFood;

import net.minecraft.item.ItemStack;

public class SaddleQuernManager {

    static final List<SaddleQuernRecipe> recipes = new ArrayList<SaddleQuernRecipe>();

    public static void addRecipe(SaddleQuernRecipe recipe) {
        recipes.add(recipe);
    }

    public static SaddleQuernRecipe getMatchingRecipe(ItemStack itemStack) {
        for (SaddleQuernRecipe recipe : recipes) {
            if (recipe.matches(itemStack)) {
                return recipe;
            }
        }

        if (itemStack.getItem() instanceof IFood) {
            QuernRecipe recipe = QuernManager.getInstance().findMatchingRecipe(itemStack);
            if (recipe != null) {
                return new SaddleQuernRecipe(recipe.getResult(), recipe.getInItem());
            }
        }

        return null;
    }

    public static boolean hasMatchingRecipe(ItemStack itemStack) {
        return getMatchingRecipe(itemStack) != null;
    }

    public static List<SaddleQuernRecipe> getRecipes() {
        return new ArrayList<SaddleQuernRecipe>(recipes);
    }

}
