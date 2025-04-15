package com.unforbidable.tfc.bids.Core.Recipes;

import com.dunk.tfc.api.Crafting.AnvilManager;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.ActionToolBinding;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;

public class RecipeHelper {

    @SuppressWarnings({"unchecked" })
    public static void handleCompositeToolRecipes() {
        List<IRecipe> compositeRecipes = new ArrayList<IRecipe>();

        List<Integer> stoneToolOreIds = getStoneToolOreIds();
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for (int i = 0; i < recipes.size(); i++) {
            IRecipe recipe = recipes.get(i);
            ItemStack output = recipe.getRecipeOutput();
            if (hasAnyOreId(output, stoneToolOreIds)) {
                IRecipe compositeRecipe = createCompositeToolRecipe(recipe);
                if (compositeRecipe != null) {
                    compositeRecipes.add(compositeRecipe);

                    recipes.remove(i--);
                    Bids.LOG.info("Original stone tool recipe removed: " + recipe.getRecipeOutput());
                }
            }
        }

        for (IRecipe recipe : compositeRecipes) {
            GameRegistry.addRecipe(recipe);
            Bids.LOG.info("Composite stone tool recipe added: " + recipe.getRecipeOutput());

            RecipeManager.addAction(new ActionToolBinding()
                .matchCraftingItem(recipe.getRecipeOutput().getItem()));
        }
    }

    private static IRecipe createCompositeToolRecipe(IRecipe recipe) {
        List<Object> input = getRecipeInput(recipe);
        if (input != null) {
            // Copy ingredients from the original recipe
            List<Object> ingredients = new ArrayList<Object>();
            for (Object obj : input) {
                if (obj != null) {
                    Object ingredient = getOriginalRecipeIngredient(obj);
                    if (ingredient != null) {
                        ingredients.add(ingredient);
                    }
                }
            }

            if (ingredients.size() == input.size()) {
                // and add binding
                ingredients.add("materialBinding");
                return new ShapelessOreRecipe(recipe.getRecipeOutput(), ingredients.toArray());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @SuppressWarnings({"unchecked" })
    private static Object getOriginalRecipeIngredient(Object obj) {
        if (obj instanceof ArrayList<?>) {
            // When ORE name is used in a recipe
            // the corresponding list of ingredients is stored,
            // but we need ore name for the new recipe,
            // so we need to find which ORE all these items belong too
            Map<Integer, Integer> oreIdMap = new HashMap<Integer, Integer>();
            for (ItemStack is : (ArrayList<ItemStack>)obj) {
                for (int oreId : OreDictionary.getOreIDs(is)) {
                    if (oreIdMap.get(oreId) != null) {
                        oreIdMap.put(oreId, oreIdMap.get(oreId) + 1);
                    } else {
                        oreIdMap.put(oreId, 1);
                    }
                }
            }

            int size = ((ArrayList<?>) obj).size();
            for (Map.Entry<Integer, Integer> val : oreIdMap.entrySet()) {
                if (size == val.getValue()) {
                    // Return the first ORE
                    // that contains all the items on the ingredient list
                    return OreDictionary.getOreName(val.getKey());
                }
            }

            return null;
        } else {
            return obj;
        }
    }

    private static List<Object> getRecipeInput(IRecipe recipe) {
        if (recipe instanceof ShapelessOreRecipe) {
            return ((ShapelessOreRecipe) recipe).getInput();
        } if (recipe instanceof ShapelessRecipes) {
            return Arrays.asList(((ShapelessRecipes) recipe).recipeItems.toArray());
        } else if (recipe instanceof ShapedOreRecipe) {
            return Arrays.asList(((ShapedOreRecipe) recipe).getInput());
        } else if (recipe instanceof ShapedRecipes) {
            return Arrays.asList((Object[])((ShapedRecipes) recipe).recipeItems);
        } else {
            Bids.LOG.warn("Composite tool recipe ignored for {} (instanceof {})", recipe.getRecipeOutput(), recipe.getClass());

            return null;
        }
    }

    private static boolean hasAnyOreId(ItemStack output, List<Integer> stoneToolOreIds) {
        for (int oreId : OreDictionary.getOreIDs(output)) {
            for (int stoneToolOreId : stoneToolOreIds) {
                if (stoneToolOreId == oreId) {
                    return true;
                }
            }
        }

        return false;
    }

    private static List<Integer> getStoneToolOreIds() {
        List<Integer> oreIds = new ArrayList<Integer>();
        for (String ore : new String[] { "itemAxeStone", "itemHammerStone", "itemKnifeStone", "itemShovelStone", "itemHoeStone", "itemJavelinStone", "itemAdzeStone", "itemDrillStone" }) {
            oreIds.add(OreDictionary.getOreID(ore));
        }
        return oreIds;
    }

    public static void applyCompositeToolBindingBonus(ItemStack tool, ItemStack binding) {
        int poorOreId = OreDictionary.getOreID("materialBindingPoor");
        int decentOreId = OreDictionary.getOreID("materialBindingDecent");
        int goodOreId = OreDictionary.getOreID("materialBindingStrong");

        int[] ids = OreDictionary.getOreIDs(binding);
        for (int id : ids) {
            if (id == poorOreId) {
                AnvilManager.setDurabilityBuff(tool, 0.1f);
            } else if (id == decentOreId) {
                AnvilManager.setDurabilityBuff(tool, 0.5f);
            } else if (id == goodOreId) {
                AnvilManager.setDurabilityBuff(tool, 1f);
            }
        }
    }

}
