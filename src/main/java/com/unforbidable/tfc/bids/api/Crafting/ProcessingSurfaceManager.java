package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ProcessingSurfaceManager {

    private static final List<ProcessingSurfaceRecipe> recipes = new ArrayList<ProcessingSurfaceRecipe>();

    public static void addRecipe(ProcessingSurfaceRecipe recipe) {
        recipes.add(recipe);
    }

    public static ProcessingSurfaceRecipe findMatchingRecipe(ItemStack input, World world, int x, int y, int z) {
        Block surfaceBlock = world.getBlock(x, y, z);
        int surfaceBlockMetadata = world.getBlockMetadata(x, y, z);
        ItemStack surface = new ItemStack(surfaceBlock, 1, surfaceBlockMetadata);

        for (ProcessingSurfaceRecipe recipe : recipes) {
            if (recipe.matchesInput(input) && recipe.matchesSurface(surface)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<ProcessingSurfaceRecipe> getRecipes() {
        return new ArrayList<ProcessingSurfaceRecipe>(recipes);
    }

}
