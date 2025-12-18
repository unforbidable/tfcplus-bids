package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SoakingSurfaceManager {

    private static final List<SoakingSurfaceRecipe> recipes = new ArrayList<SoakingSurfaceRecipe>();

    public static void addRecipe(SoakingSurfaceRecipe recipe) {
        recipes.add(recipe);
    }

    public static SoakingSurfaceRecipe findMatchingRecipe(ItemStack input, World world, int x, int y, int z) {
        Block surfaceBlock = world.getBlock(x, y, z);
        int surfaceBlockMetadata = world.getBlockMetadata(x, y, z);
        ItemStack surface = new ItemStack(surfaceBlock, 1, surfaceBlockMetadata);

        for (SoakingSurfaceRecipe recipe : recipes) {
            if (recipe.matchesInput(input) && recipe.matchesSurface(surface)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<SoakingSurfaceRecipe> getRecipes() {
        return new ArrayList<SoakingSurfaceRecipe>(recipes);
    }

}
