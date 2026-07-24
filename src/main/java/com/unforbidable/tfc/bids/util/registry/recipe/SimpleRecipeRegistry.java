package com.unforbidable.tfc.bids.util.registry.recipe;

import com.unforbidable.tfc.bids.api.util.SimpleRecipeMatcher;

public class SimpleRecipeRegistry<R extends SimpleRecipeMatcher<I>, I> extends RecipeRegistry<R> {

    public R findMatchingRecipe(I ingredient) {
        return super.findMatchingRecipe(r -> r.matches(ingredient));
    }

}
