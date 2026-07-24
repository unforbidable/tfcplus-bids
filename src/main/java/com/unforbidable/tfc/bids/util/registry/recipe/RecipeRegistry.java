package com.unforbidable.tfc.bids.util.registry.recipe;

import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import java.util.function.Predicate;

public class RecipeRegistry<T> extends ListRegistry<T> {

    public T findMatchingRecipe(Predicate<T> predicate) {
        return stream().filter(predicate)
            .findFirst().orElse(null);
    }

}
