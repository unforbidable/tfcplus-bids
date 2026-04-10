package com.unforbidable.tfc.bids.api.Crafting.Builders;

import com.unforbidable.tfc.bids.api.Crafting.DryingRackRecipe;

public class DryingRackRecipeBuilder extends DryingRecipeBuilder {

    protected boolean requiresTyingEquipment;

    public DryingRackRecipeBuilder tied() {
        this.requiresTyingEquipment = true;

        return this;
    }

    @Override
    public DryingRackRecipe build() {
        return new DryingRackRecipe(inputItem, outputItem, destroyedOutputItem, duration,
            requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet,
            requiresTyingEquipment);
    }

}
