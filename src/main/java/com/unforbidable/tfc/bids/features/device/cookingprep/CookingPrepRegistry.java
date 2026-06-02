package com.unforbidable.tfc.bids.features.device.cookingprep;

import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepRecipe;
import com.unforbidable.tfc.bids.util.registry.recipe.SimpleRecipeRegistry;
import net.minecraft.item.ItemStack;

public class CookingPrepRegistry {

    public static final SimpleRecipeRegistry<CookingPrepRecipe, ItemStack[]> recipes = new SimpleRecipeRegistry<>();

}
