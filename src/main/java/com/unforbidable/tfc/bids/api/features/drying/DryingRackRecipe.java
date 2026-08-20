package com.unforbidable.tfc.bids.api.features.drying;

import net.minecraft.item.ItemStack;

public class DryingRackRecipe extends DryingRecipe {

    final boolean requiresTyingEquipment;

    public DryingRackRecipe(ItemStack inputItem, ItemStack outputItem, ItemStack destroyedOutputItem, int duration, boolean requiresDry, boolean requiresWet, boolean requiresCover, boolean requiresWarm, boolean requiresFreezing, boolean requiresNotWet, boolean requiresSmoke, int canSmokeDuration, boolean requiresTyingEquipment) {
        super(inputItem, outputItem, destroyedOutputItem, duration, requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet, requiresSmoke, canSmokeDuration);

        this.requiresTyingEquipment = requiresTyingEquipment;
    }

    public static DryingRackRecipeBuilder builder() {
        return new DryingRackRecipeBuilder();
    }

    public boolean getRequiresTyingEquipment() {
        return requiresTyingEquipment;
    }

}
