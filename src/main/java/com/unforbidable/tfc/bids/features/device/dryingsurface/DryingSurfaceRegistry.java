package com.unforbidable.tfc.bids.features.device.dryingsurface;

import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.drying.IDryingItemRenderInfo;
import com.unforbidable.tfc.bids.util.registry.item.ItemRegistry;
import com.unforbidable.tfc.bids.util.registry.recipe.SimpleRecipeRegistry;
import net.minecraft.item.ItemStack;

public class DryingSurfaceRegistry {

    public static final SimpleRecipeRegistry<DryingSurfaceRecipe, ItemStack> recipes = new SimpleRecipeRegistry<>();
    public static final ItemRegistry<IDryingItemRenderInfo> render = new ItemRegistry<>();

}
