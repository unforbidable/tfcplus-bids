package com.unforbidable.tfc.bids.features.device.cookingprep.main;

import com.dunk.tfc.TileEntities.TEBarrel;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepRecipe;
import com.unforbidable.tfc.bids.features.device.cookingprep.CookingPrepRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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

    public static void placeCookingPrep(World world, int x, int y, int z) {
        world.setBlock(x, y, z, BidsBlocks.cookingPrep);
    }

    public static boolean isValidCookingPrepLocation(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z) && world.isSideSolid(x, y - 1, z, ForgeDirection.UP)) {
            Block block = world.getBlock(x, y - 1, z);
            if (block.getMaterial() == Material.rock || block.getMaterial() == Material.wood || block.getMaterial() == Material.iron) {
                world.setBlock(x, y, z, BidsBlocks.cookingPrep);
                return true;
            }
        }

        return false;
    }

    public static boolean canGrowYeast(ItemStack itemStack) {
        return !Food.isBrined(itemStack) && !Food.isPickled(itemStack) && !Food.isSalted(itemStack) &&
            CookingPrepRegistry.yeast.has(i -> i == itemStack.getItem());
    }

}
