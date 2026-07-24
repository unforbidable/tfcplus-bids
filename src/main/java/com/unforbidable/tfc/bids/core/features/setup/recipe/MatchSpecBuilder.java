package com.unforbidable.tfc.bids.core.features.setup.recipe;

import com.unforbidable.tfc.bids.core.crafting.MatchingRecipe;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MatchSpecBuilder {

    private final Predicate<MatchingRecipe> predicate;
    private Consumer<MatchingRecipe> consumer;

    public MatchSpecBuilder(Predicate<MatchingRecipe> predicate) {
        this.predicate = predicate;
    }

    public void edit(Consumer<MatchingRecipe> consumer) {
        this.consumer = consumer;
    }

    public MatchSpec build() {
        return new MatchSpec(predicate, consumer);
    }

}
