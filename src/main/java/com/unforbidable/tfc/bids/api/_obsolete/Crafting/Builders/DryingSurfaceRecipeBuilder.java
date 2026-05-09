package com.unforbidable.tfc.bids.api._obsolete.Crafting.Builders;

import com.unforbidable.tfc.bids.api._obsolete.Crafting.DryingSurfaceRecipe;

public class DryingSurfaceRecipeBuilder extends DryingRecipeBuilder {

    @Override
    public DryingSurfaceRecipe build() {
        return new DryingSurfaceRecipe(inputItem, outputItem, destroyedOutputItem, duration,
            requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet);
    }

}
