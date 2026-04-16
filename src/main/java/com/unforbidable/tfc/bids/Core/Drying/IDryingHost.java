package com.unforbidable.tfc.bids.Core.Drying;

import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;

public interface IDryingHost {

    DryingItem[] getDryingStorage();
    DryingRecipe getDryingRecipe(DryingItem item);
    float getWetnessIncreaseRate();
    float getWetnessReductionRate();
    void notifyClientChanges();

}
