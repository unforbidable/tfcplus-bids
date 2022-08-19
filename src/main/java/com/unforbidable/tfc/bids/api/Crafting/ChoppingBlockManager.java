package com.unforbidable.tfc.bids.api.Crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unforbidable.tfc.bids.Bids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ChoppingBlockManager {

    static int nextAvailableId = 0;

    static final Map<Integer, String> idToNameMap = new HashMap<Integer, String>();
    static final Map<String, Integer> nameToIdMap = new HashMap<String, Integer>();

    static final List<ChoppingBlockRecipe> recipes = new ArrayList<ChoppingBlockRecipe>();

    static final Map<Integer, List<ChoppingBlockRecipe>> cachedRecipes = new HashMap<Integer, List<ChoppingBlockRecipe>>();
    static final Map<Integer, List<ItemStack>> cachedTools = new HashMap<Integer, List<ItemStack>>();

    public static int getChoppingBlockId(String choppingBlockName) {
        if (nameToIdMap.containsKey(choppingBlockName)) {
            return nameToIdMap.get(choppingBlockName);
        }

        int nextId = nextAvailableId++;

        nameToIdMap.put(choppingBlockName, nextId);
        idToNameMap.put(nextId, choppingBlockName);

        return nextId;
    }

    public static String getChoppingBlockName(int choppingBlockId) {
        return idToNameMap.get(choppingBlockId);
    }

    public static void addRecipe(ChoppingBlockRecipe recipe) {
        recipes.add(recipe);

        cachedRecipes.clear();
        cachedTools.clear();
    }

    public static ChoppingBlockRecipe findMatchingRecipe(int choppingBlockId, ItemStack tool, ItemStack input) {
        for (ChoppingBlockRecipe recipe : getChoppingBlockRecipes(choppingBlockId)) {
            if (recipe.match(tool, input)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<ChoppingBlockRecipe> getChoppingBlockRecipes(int choppingBlockId) {
        List<ChoppingBlockRecipe> list = cachedRecipes.get(choppingBlockId);

        if (list == null) {
            list = new ArrayList<ChoppingBlockRecipe>();

            for (ChoppingBlockRecipe recipe : recipes) {
                if (recipe.getWorkbenchId() == choppingBlockId) {
                    list.add(recipe);
                }
            }

            cachedRecipes.put(choppingBlockId, list);
        }

        return list;
    }

    public static List<ItemStack> getWorkbenchTools(int choppingBlockId) {
        List<ItemStack> list = cachedTools.get(choppingBlockId);

        if (list == null) {
            list = new ArrayList<ItemStack>();

            for (ChoppingBlockRecipe recipe : getChoppingBlockRecipes(choppingBlockId)) {
                for (ItemStack tool : OreDictionary.getOres(recipe.toolOreName, false)) {
                    boolean alreadyAdded = false;

                    for (ItemStack item : list) {
                        if (item.getItem() == tool.getItem()
                                && (item.getItemDamage() == tool.getItemDamage()
                                        || item.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                            alreadyAdded = true;

                            break;
                        }
                    }

                    if (!alreadyAdded) {
                        list.add(tool);

                        Bids.LOG.debug("Tool " + tool.getDisplayName()
                                + " added to chopping block: " + getChoppingBlockName(choppingBlockId));
                    }
                }
            }

            cachedTools.put(choppingBlockId, list);
        }

        return list;
    }

    public static boolean isChoppingBlockTool(int choppingBlockId, ItemStack item) {
        for (ItemStack tool : getWorkbenchTools(choppingBlockId)) {
            if (item.getItem() == tool.getItem()
                    && (item.getItemDamage() == tool.getItemDamage()
                            || tool.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                Bids.LOG.debug("Item " + item.getDisplayName()
                        + " is tool for chopping block " + getChoppingBlockName(choppingBlockId));

                return true;
            }
        }

        return false;
    }

    public static boolean isChoppingBlockInput(int choppingBlockId, ItemStack item) {
        for (ChoppingBlockRecipe recipe : getChoppingBlockRecipes(choppingBlockId)) {
            if (item.getItem() == recipe.input.getItem()
                    && (item.getItemDamage() == recipe.input.getItemDamage()
                            || recipe.input.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                Bids.LOG.debug("Item " + item.getDisplayName()
                        + " is input for chopping block " + getChoppingBlockName(choppingBlockId));

                return true;
            }
        }

        return false;
    }

}
