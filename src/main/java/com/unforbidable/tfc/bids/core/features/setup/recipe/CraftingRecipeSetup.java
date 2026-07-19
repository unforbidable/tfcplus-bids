package com.unforbidable.tfc.bids.core.features.setup.recipe;

import com.unforbidable.tfc.bids.core.crafting.ActionableRecipe;
import java.util.List;

public class CraftingRecipeSetup {

    public final List<ActionableRecipe> recipes;
    public final List<MatchSpec> matchers;

    public CraftingRecipeSetup(List<ActionableRecipe> recipes, List<MatchSpec> matchers) {
        this.recipes = recipes;
        this.matchers = matchers;
    }

}
