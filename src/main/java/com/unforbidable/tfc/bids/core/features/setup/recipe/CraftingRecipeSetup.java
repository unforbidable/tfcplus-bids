package com.unforbidable.tfc.bids.core.features.setup.recipe;

import com.unforbidable.tfc.bids.core.crafting.ActionableRecipe;
import com.unforbidable.tfc.bids.core.crafting.MatchingRecipe;

import java.util.List;
import java.util.function.Consumer;

public class CraftingRecipeSetup {

    public final List<ActionableRecipe> recipes;
    public final List<Consumer<MatchingRecipe>> matchers;

    public CraftingRecipeSetup(List<ActionableRecipe> recipes, List<Consumer<MatchingRecipe>> matchers) {
        this.recipes = recipes;
        this.matchers = matchers;
    }

}
