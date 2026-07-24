package com.unforbidable.tfc.bids.features.building.carving;

import com.unforbidable.tfc.bids.api.features.carving.Carvable;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipe;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import com.unforbidable.tfc.bids.util.registry.recipe.RecipeRegistry;

public class CarvingRegistry {

    public static final ListRegistry<Carvable> carvable = new ListRegistry<>();
    public static final RecipeRegistry<CarvingRecipe> recipes = new RecipeRegistry<>();

}
