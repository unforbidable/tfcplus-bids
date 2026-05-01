package com.unforbidable.tfc.bids.Core.Crafting;

import com.unforbidable.tfc.bids.Core.Crafting.Matchers.*;

public class MatchingRecipe {

    public final RecipeAccessor recipe;
    public final ObjectMatcher output;
    public final RecipeInputMatcher input;

    private MatchingRecipe(RecipeAccessor recipe, ObjectMatcher output, RecipeInputMatcher input) {
        this.recipe = recipe;
        this.output = output;
        this.input = input;
    }

    public static MatchingRecipe of(RecipeAccessor recipe) {
        RecipeMatcher matcher = new RecipeMatcher(recipe);

        return new MatchingRecipe(recipe, matcher.getOutputMatcher(), matcher.getInputMatchers());
    }

    public void remove() {
        RecipeManager.session.markForRemoval(this);
    }

    public CloningRecipe replace() {
        return clone(true);
    }

    public CloningRecipe clone(boolean removeOriginalRecipe) {
        if (removeOriginalRecipe) {
            RecipeManager.session.markForRemoval(this);
        }

        CloningRecipe cloningRecipe = new CloningRecipe(recipe);
        RecipeManager.session.submitCloningRecipe(cloningRecipe);

        return cloningRecipe;
    }

}
