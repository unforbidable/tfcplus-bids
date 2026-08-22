package com.unforbidable.tfc.bids.api.features.drying;

import net.minecraft.item.ItemStack;

public class DryingFrameRecipe extends DryingRecipe {

    final boolean consumesTypingEquipment;

    public DryingFrameRecipe(ItemStack inputItem, ItemStack outputItem, ItemStack destroyedOutputItem, int duration, boolean requiresDry, boolean requiresWet, boolean requiresCover, boolean requiresWarm, boolean requiresFreezing, boolean requiresNotWet, boolean requiresSmoke, int canSmokeDuration, boolean consumesTypingEquipment) {
        super(inputItem, outputItem, destroyedOutputItem, duration, requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet, requiresSmoke, canSmokeDuration);
        this.consumesTypingEquipment = consumesTypingEquipment;
    }

    public static DryingFrameRecipeBuilder builder() {
        return new DryingFrameRecipeBuilder();
    }

    public boolean isConsumesTypingEquipment() {
        return consumesTypingEquipment;
    }

}
