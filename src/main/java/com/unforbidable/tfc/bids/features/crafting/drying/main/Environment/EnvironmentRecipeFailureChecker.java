package com.unforbidable.tfc.bids.features.crafting.drying.main.Environment;

import com.unforbidable.tfc.bids.api.features.drying.DryingRecipe;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IDryingEnvironment;

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
