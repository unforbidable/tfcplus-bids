package com.unforbidable.tfc.bids.api.features.drying;

public class DryingFrameRecipeBuilder extends DryingRecipeBuilder {

    protected boolean consumesTyingEquipment;

    public DryingFrameRecipeBuilder consumesTyingEquipment() {
        this.consumesTyingEquipment = true;

        return this;
    }

    @Override
    public DryingFrameRecipe build() {
        return new DryingFrameRecipe(inputItem, outputItem, destroyedOutputItem, duration,
            requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet, requiresSmoke, canSmokeDuration,
            consumesTyingEquipment);
    }

}
