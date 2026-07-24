package com.unforbidable.tfc.bids.features.device.woodpile;

import com.unforbidable.tfc.bids.api.features.woodpile.Crackable;
import com.unforbidable.tfc.bids.api.features.woodpile.SeasoningRecipe;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import com.unforbidable.tfc.bids.util.registry.item.ItemRegistry;
import com.unforbidable.tfc.bids.util.registry.recipe.SimpleRecipeRegistry;
import net.minecraft.item.ItemStack;

public class WoodpileRegistry {

    public static final ItemRegistry<WoodpileRenderable> renderable = new ItemRegistry<>();
    public static final ListRegistry<Crackable> crackable = new ListRegistry<>();

    public static final SimpleRecipeRegistry<SeasoningRecipe, ItemStack> seasoning = new SimpleRecipeRegistry<>();

}
