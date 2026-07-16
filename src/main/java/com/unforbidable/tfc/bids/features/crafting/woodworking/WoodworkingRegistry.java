package com.unforbidable.tfc.bids.features.crafting.woodworking;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingPlan;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingTool;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingRecipe;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import com.unforbidable.tfc.bids.util.registry.recipe.SimpleRecipeRegistry;
import net.minecraft.item.ItemStack;

public class WoodworkingRegistry {

    public static final ListRegistry<WoodworkingMaterial> materials = new ListRegistry<>();
    public static final ListRegistry<WoodworkingTool> tools = new ListRegistry<>();
    public static final ListRegistry<WoodworkingPlan> plans = new ListRegistry<>();
    public static final SimpleRecipeRegistry<WoodworkingRecipe, ItemStack> recipes = new SimpleRecipeRegistry<>();

}
