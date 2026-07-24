package com.unforbidable.tfc.bids.core.features.setup.recipe;

import com.unforbidable.tfc.bids.core.crafting.MatchingRecipe;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MatchSpec {

    public final Predicate<MatchingRecipe> matching;
    public final Consumer<MatchingRecipe> cloning;

    public MatchSpec(Predicate<MatchingRecipe> matching, Consumer<MatchingRecipe> cloning) {
        this.matching = matching;
        this.cloning = cloning;
    }

}
