package com.unforbidable.tfc.bids.Core.Drying.Environment;

import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingEnvironment;

public class EnvironmentRecipeFailureChecker {

    private final IDryingEnvironment env;
    private final DryingRecipe recipe;

    public EnvironmentRecipeFailureChecker(IDryingEnvironment env, DryingRecipe recipe) {
        this.env = env;
        this.recipe = recipe;
    }

    public float checkFailure() {
        return checkNotWet();
    }

    private float checkNotWet() {
        return recipe.isRequiresNotWet() ? env.getWetness() : 0;
    }

}
