package com.unforbidable.tfc.bids.features.device.cookingprep.main;

import com.unforbidable.tfc.bids.api._obsolete.BidsRegistry;
import com.unforbidable.tfc.bids.api._obsolete.Crafting.PrepRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CookingPrepHelper {

    public static boolean isValidPrepVessel(ItemStack is) {
        for (PrepRecipe recipe : BidsRegistry.PREP_RECIPES) {
            if (recipe.doesVesselMatch(is)) {
                return true;
            }
        }

        return false;
    }

    public static List<PrepRecipe> getRecipesUsingVessel(ItemStack is) {
        List<PrepRecipe> list = new ArrayList<PrepRecipe>();

        for (PrepRecipe recipe : BidsRegistry.PREP_RECIPES) {
            if (recipe.doesVesselMatch(is)) {
                list.add(recipe);
            }
        }

        return list;
    }

}
