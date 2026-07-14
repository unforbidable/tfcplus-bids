package com.unforbidable.tfc.bids.api.features.drying;

import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRecipeBuilder;

public class DryingSurfaceRecipeBuilder extends DryingRecipeBuilder {

    @Override
    public DryingSurfaceRecipe build() {
        return new DryingSurfaceRecipe(inputItem, outputItem, destroyedOutputItem, duration,
            requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet);
    }

}
