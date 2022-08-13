package com.unforbidable.tfc.bids.Core.DryingRack;

import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;

public class DryingRackItemInfo {

    public final DryingRackItem item;
    public final DryingRecipe recipe;
    public final int dryingTicksRemaining;

    public DryingRackItemInfo(DryingRackItem item, DryingRecipe recipe, int dryingTicksRemaining) {
        this.item = item;
        this.recipe = recipe;
        this.dryingTicksRemaining = dryingTicksRemaining;
    }

}
