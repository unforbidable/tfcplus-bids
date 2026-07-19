package com.unforbidable.tfc.bids.features.crafting.woodworking.main;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionGroup;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingPlan;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingRecipe;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingTool;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspacePlan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WoodworkingHelper {

    public static boolean isValidWoodworkingMaterial(ItemStack itemstack) {
        return getWoodworkingMaterial(itemstack) != null;
    }

    public static List<WorkspacePlan> getWoodworkingPlans(ItemStack itemStack) {
        List<WorkspacePlan> plans = new ArrayList<>();

        for (WoodworkingRecipe recipe : WoodworkingHelper.findMatchingRecipes(itemStack)) {
            WoodworkingPlan plan = WoodworkingRegistry.plans.get(p -> p.getName().equals(recipe.getPlanName()));
            if (plan != null) {
                plans.add(new WorkspacePlan(plan, recipe, itemStack));
            }
        }

        return plans;
    }

    public static List<WoodworkingRecipe> findMatchingRecipes(ItemStack itemStack) {
        List<WoodworkingRecipe> matching = new ArrayList<>();

        for (WoodworkingRecipe recipe : WoodworkingRegistry.recipes) {
            if (recipe.matches(itemStack)) {
                matching.add(recipe);
            }
        }

        return matching;
    }

    public static WoodworkingAction getActionByName(String actionName) {
        return WoodworkingRegistry.tools.stream()
            .flatMap(t -> Arrays.stream(t.getActionGroups()))
            .flatMap(g -> Arrays.stream(g.getActions()))
            .filter(a -> actionName.equals(a.getName()))
            .findAny()
            .orElse(null);
    }

    public static WoodworkingMaterial findMaterial(List<ItemStack> ingredients) {
        // Find an item that is a registered material
        // in case the ore recipe contains invalid materials
        for (ItemStack ingredient : ingredients) {
            WoodworkingMaterial material = getWoodworkingMaterial(ingredient);
            if (material != null) {
                return material;
            }
        }

        return null;
    }

    public static WoodworkingMaterial getWoodworkingMaterial(ItemStack itemStack) {
        for (WoodworkingMaterial material : WoodworkingRegistry.materials) {
            if (isOreList(itemStack, OreDictionary.getOres(material.getOreName()))) {
                return material;
            }
        }

        return null;
    }

    private static boolean isOreList(ItemStack itemStack, List<ItemStack> ores) {
        for (ItemStack ore : ores) {
            if (ore.getItem() == itemStack.getItem()
                && (ore.getItemDamage() == itemStack.getItemDamage()
                || ore.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                return true;
            }
        }

        return false;
    }

    public static float getActionToolDamageByName(String actionName) {
        for (WoodworkingTool tool : WoodworkingRegistry.tools) {
            for (WoodworkingActionGroup group : tool.getActionGroups()) {
                for (WoodworkingAction action : group.getActions()) {
                    if (actionName.equals(action.getName())) {
                        return group.getToolDamage();
                    }
                }
            }
        }

        return 0;
    }

    public static WoodworkingTool getWoodworkingTool(ItemStack itemStack) {
        for (WoodworkingTool tool : WoodworkingRegistry.tools) {
            if (isOreList(itemStack, OreDictionary.getOres(tool.getOreName()))) {
                return tool;
            }
        }

        return null;

    }

}
