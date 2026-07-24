package com.unforbidable.tfc.bids.features.crafting.cooking;

import com.unforbidable.tfc.bids.api.features.cooking.CookingMixture;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import com.unforbidable.tfc.bids.util.registry.item.ItemRegistry;
import com.unforbidable.tfc.bids.util.registry.recipe.RecipeRegistry;
import net.minecraft.item.Item;

public class CookingRegistry {

    public static final RecipeRegistry<CookingRecipe> recipes = new RecipeRegistry<>();
    public static final ListRegistry<CookingMixture> mixtures = new ListRegistry<>();
    public static final ItemRegistry<Item> ingredientOverrides = new ItemRegistry<>();

}
