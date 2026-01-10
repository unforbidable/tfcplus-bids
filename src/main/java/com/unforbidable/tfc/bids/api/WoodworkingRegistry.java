package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Interfaces.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class WoodworkingRegistry {

    private final static Map<String, IWoodworkingTool> tools = new HashMap<String, IWoodworkingTool>();
    private final static Map<Item, String> toolItems = new HashMap<Item, String>();
    private final static Map<Integer, String> toolOreIds = new HashMap<Integer, String>();

    private final static Map<String, IWoodworkingMaterial> materials = new HashMap<String, IWoodworkingMaterial>();
    private final static Map<Item, String> materialItems = new HashMap<Item, String>();
    private final static Map<Integer, String> materialOreIds = new HashMap<Integer, String>();

    private final static Map<String, IWoodworkingPlan> plans = new HashMap<String, IWoodworkingPlan>();

    private final static HashMap<String, IWoodworkingAction> actions = new HashMap<String, IWoodworkingAction>();
    private final static HashMap<String, Float> actionToolDamages = new HashMap<String, Float>();

    public static void registerTool(String name, IWoodworkingTool tool) {
        tools.put(name, tool);

        for (IWoodworkingActionGroup group : tool.getActionGroups()) {
            for (IWoodworkingAction action : group.getActions()) {
                if (!actions.containsKey(action.getName())) {
                    actions.put(action.getName(), action);
                    actionToolDamages.put(action.getName(), group.getToolDamage());
                }
            }
        }
    }

    public static void addItemAsTool(String name, Item item) {
        toolItems.put(item, name);
    }

    public static void addOreAsTool(String name, String ore) {
        toolOreIds.put(OreDictionary.getOreID(ore), name);
    }

    public static IWoodworkingTool findToolForItem(Item item) {
        if (toolItems.containsKey(item)) {
            return getToolByName(toolItems.get(item));
        }

        for (int oreId : OreDictionary.getOreIDs(new ItemStack(item))) {
            if (toolOreIds.containsKey(oreId)) {
                return getToolByName(toolOreIds.get(oreId));
            }
        }

        return null;
    }

    public static IWoodworkingTool getToolByName(String name) {
        return tools.get(name);
    }

    public static void registerMaterial(String name, IWoodworkingMaterial material) {
        materials.put(name, material);
    }

    public static void addItemAsMaterial(String name, Item item) {
        materialItems.put(item, name);
    }

    public static void addOreAsMaterial(String name, String ore) {
        materialOreIds.put(OreDictionary.getOreID(ore), name);
    }

    public static IWoodworkingMaterial findMaterialForItem(Item item) {
        if (materialItems.containsKey(item)) {
            return getMaterialByName(materialItems.get(item));
        }

        for (int oreId : OreDictionary.getOreIDs(new ItemStack(item))) {
            if (materialOreIds.containsKey(oreId)) {
                return getMaterialByName(materialOreIds.get(oreId));
            }
        }

        return null;
    }

    public static IWoodworkingMaterial getMaterialByName(String name) {
        return materials.get(name);
    }

    public static void registerPlan(String name, IWoodworkingPlan plan) {
        plans.put(name, plan);
    }

    public static IWoodworkingPlan getPlanByName(String name) {
        return plans.get(name);
    }

    public static IWoodworkingAction getActionByName(String actionName) {
        return actions.get(actionName);
    }

    public static float getActionToolDamageByName(String actionName) {
        return actionToolDamages.get(actionName);
    }

}
