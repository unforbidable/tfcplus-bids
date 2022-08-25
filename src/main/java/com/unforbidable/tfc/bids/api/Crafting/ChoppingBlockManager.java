package com.unforbidable.tfc.bids.api.Crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ChoppingBlockManager {

    static int nextAvailableId = 0;

    static final List<ChoppingBlockRecipe> recipes = new ArrayList<ChoppingBlockRecipe>();

    public static void addRecipe(ChoppingBlockRecipe recipe) {
        recipes.add(recipe);
    }

    public static ChoppingBlockRecipe findMatchingRecipe(ItemStack choppingBlock, ItemStack tool, ItemStack input) {
        for (ChoppingBlockRecipe recipe : recipes) {
            if (recipe.matches(choppingBlock, tool, input)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<ItemStack> getWorkbenchTools(ItemStack choppingBlock) {
        List<ItemStack> list = new ArrayList<ItemStack>();

        for (ChoppingBlockRecipe recipe : recipes) {
            if (recipe.matchesChoppingBlock(choppingBlock)) {
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
                    }
                }
            }
        }

        return list;
    }

    public static boolean isChoppingBlockTool(ItemStack choppingBlock, ItemStack tool) {
        for (ChoppingBlockRecipe recipe : recipes) {
            if (recipe.matchesChoppingBlock(choppingBlock) && recipe.matchesTool(tool)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isChoppingBlockInput(ItemStack choppingBlock, ItemStack item) {
        for (ChoppingBlockRecipe recipe : recipes) {
            if (recipe.matchesChoppingBlock(choppingBlock) && recipe.matchesInput(item)) {
                return true;
            }
        }

        return false;
    }

    public static List<ChoppingBlockRecipe> getRecipes() {
        return new ArrayList<ChoppingBlockRecipe>(recipes);
    }

}
