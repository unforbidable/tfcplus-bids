package com.unforbidable.tfc.bids.Core.Woodworking;

import com.unforbidable.tfc.bids.Core.Woodworking.Plans.PlanInstance;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Crafting.WoodworkingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingPlan;
import com.unforbidable.tfc.bids.api.WoodworkingRegistry;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WoodworkingHelper {

    public static boolean isValidWoodworkingMaterial(ItemStack itemstack) {
        return WoodworkingRegistry.findMaterialForItem(itemstack.getItem()) != null;
    }

    public static IWoodworkingMaterial getWoodworkingMaterial(ItemStack itemStack) {
        return WoodworkingRegistry.findMaterialForItem(itemStack.getItem());
    }

    public static List<PlanInstance> getWoodworkingPlans(ItemStack itemStack) {
        List<PlanInstance> plans = new ArrayList<PlanInstance>();

        for (WoodworkingRecipe recipe : WoodworkingHelper.findMatchingRecipes(itemStack)) {
            IWoodworkingPlan plan = WoodworkingRegistry.getPlanByName(recipe.getPlanName());
            if (plan != null) {
                plans.add(new PlanInstance(recipe.getPlanName(), plan, recipe.getResult(itemStack)));
            }
        }

        return plans;
    }

    public static List<WoodworkingRecipe> findMatchingRecipes(ItemStack itemStack) {
        List<WoodworkingRecipe> matching = new ArrayList<WoodworkingRecipe>();

        for (WoodworkingRecipe recipe : BidsRegistry.WOODWORKING_RECIPES) {
            if (recipe.matches(itemStack)) {
                matching.add(recipe);
            }
        }

        return matching;
    }

}
