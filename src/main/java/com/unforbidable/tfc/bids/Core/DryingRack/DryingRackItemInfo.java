package com.unforbidable.tfc.bids.Core.DryingRack;

import com.unforbidable.tfc.bids.api.Crafting.DryingRackRecipe;

public class DryingRackItemInfo {

    public final DryingRackItem item;
    public final DryingRackRecipe recipe;
    public final int dryingTicksRemaining;

    public DryingRackItemInfo(DryingRackItem item, DryingRackRecipe recipe, int dryingTicksRemaining) {
        this.item = item;
        this.recipe = recipe;
        this.dryingTicksRemaining = dryingTicksRemaining;
    }

}
