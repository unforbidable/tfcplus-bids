package com.unforbidable.tfc.bids.api.features.drying;

public class DryingSurfaceRecipeBuilder extends DryingRecipeBuilder {

    @Override
    public DryingSurfaceRecipe build() {
        return new DryingSurfaceRecipe(inputItem, outputItem, destroyedOutputItem, duration,
            requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet);
    }

}
