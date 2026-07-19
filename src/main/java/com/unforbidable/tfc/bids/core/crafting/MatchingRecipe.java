package com.unforbidable.tfc.bids.core.crafting;

import com.unforbidable.tfc.bids.core.crafting.matchers.ObjectMatcher;
import com.unforbidable.tfc.bids.core.crafting.matchers.RecipeInputMatcher;
import com.unforbidable.tfc.bids.core.crafting.matchers.RecipeMatcher;

public class MatchingRecipe {

    public final RecipeManagerSession session;
    public final RecipeAccessor recipe;
    public final ObjectMatcher output;
    public final RecipeInputMatcher input;

    private MatchingRecipe(RecipeManagerSession session, RecipeAccessor recipe, ObjectMatcher output, RecipeInputMatcher input) {
        this.session = session;
        this.recipe = recipe;
        this.output = output;
        this.input = input;
    }

    public static MatchingRecipe of(RecipeManagerSession session, RecipeAccessor recipe) {
        RecipeMatcher matcher = new RecipeMatcher(recipe);

        return new MatchingRecipe(session, recipe, matcher.getOutputMatcher(), matcher.getInputMatchers());
    }

    public void remove() {
        session.markForRemoval(this);
    }

    public CloningRecipe replace() {
        return clone(true);
    }

    public CloningRecipe clone(boolean removeOriginalRecipe) {
        if (removeOriginalRecipe) {
            session.markForRemoval(this);
        }

        CloningRecipe cloningRecipe = new CloningRecipe(recipe);
        session.submitCloningRecipe(cloningRecipe);

        return cloningRecipe;
    }

}
