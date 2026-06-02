package com.unforbidable.tfc.bids.features.device.cookingprep.main;

import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepRecipe;
import java.util.ArrayList;
import java.util.List;
import com.unforbidable.tfc.bids.features.device.cookingprep.CookingPrepRegistry;
import net.minecraft.item.ItemStack;

public class CookingPrepHelper {

    public static boolean isValidPrepVessel(ItemStack is) {
        for (CookingPrepRecipe recipe : CookingPrepRegistry.recipes) {
            if (recipe.doesVesselMatch(is)) {
                return true;
            }
        }

        return false;
    }

    public static List<CookingPrepRecipe> getRecipesUsingVessel(ItemStack is) {
        List<CookingPrepRecipe> list = new ArrayList<CookingPrepRecipe>();

        for (CookingPrepRecipe recipe : CookingPrepRegistry.recipes) {
            if (recipe.doesVesselMatch(is)) {
                list.add(recipe);
            }
        }

        return list;
    }

}
