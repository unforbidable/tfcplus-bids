package com.unforbidable.tfc.bids.api.Crafting;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class CarvingManager {

    static final List<CarvingRecipe> recipes = new ArrayList<CarvingRecipe>();

    public static void addRecipe(CarvingRecipe recipe) {
        recipes.add(recipe);
    }

    public static CarvingRecipe findMatchingRecipe(int carvedBlockId, int carvedBlockMetadata,
            CarvingBitMap carvedBits) {
        final Block block = Block.getBlockById(carvedBlockId);
        final ItemStack itemStack = new ItemStack(block, 1, carvedBlockMetadata);

        for (CarvingRecipe recipe : recipes) {
            if (recipe.matchCarving(itemStack, carvedBits)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<CarvingRecipe> getRecipes() {
        return new ArrayList<CarvingRecipe>(recipes);
    }

}
