package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Interfaces.IProcessingSurfaceIconProvider;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ProcessingSurfaceManager {

    private static final List<ProcessingSurfaceRecipe> recipes = new ArrayList<ProcessingSurfaceRecipe>();
    private static final List<IProcessingSurfaceIconProvider> iconProviders = new ArrayList<IProcessingSurfaceIconProvider>();

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

    public static void registerIconProvider(IProcessingSurfaceIconProvider iconProvider) {
        iconProviders.add(iconProvider);
    }

    public static IIcon registerIcon(IIconRegister registerer, ItemStack is) {
        for (IProcessingSurfaceIconProvider provider : iconProviders) {
            IIcon icon = provider.registerProcessingSurfaceIcon(registerer, is);
            if (icon != null) {
                return icon;
            }
        }

        return null;
    }

}
