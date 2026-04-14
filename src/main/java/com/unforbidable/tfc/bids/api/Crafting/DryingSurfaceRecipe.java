package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Crafting.Builders.DryingSurfaceRecipeBuilder;
import net.minecraft.item.ItemStack;

public class DryingSurfaceRecipe extends DryingRecipe {

    public DryingSurfaceRecipe(ItemStack inputItem, ItemStack outputItem, ItemStack destroyedOutputItem, int duration, boolean requiresDry, boolean requiresWet, boolean requiresCover, boolean requiresWarm, boolean requiresFreezing, boolean requiresNotWet) {
        super(inputItem, outputItem, destroyedOutputItem, duration, requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet);
    }

    public static DryingSurfaceRecipeBuilder builder() {
        return new DryingSurfaceRecipeBuilder();
    }

}
