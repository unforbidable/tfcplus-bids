package com.unforbidable.tfc.bids.api._obsolete.Registry;

import com.unforbidable.tfc.bids.api._obsolete.Interfaces.ISimpleRecipeMatcher;

public class SimpleRecipeRegistry<R extends ISimpleRecipeMatcher<I>, I> extends RecipeRegistry<R> {

    public SimpleRecipeRegistry(String name) {
        super(name);
    }

    public R findMatchingRecipe(I ingredient) {
        return super.findMatchingRecipe(r -> r.matches(ingredient));
    }

}
