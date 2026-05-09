package com.unforbidable.tfc.bids.features.crafting.drying.main;

import com.unforbidable.tfc.bids.api._obsolete.Crafting.DryingRecipe;

public interface IDryingHost {

    DryingItem[] getDryingStorage();
    DryingRecipe getDryingRecipe(DryingItem item);
    float getWetnessIncreaseRate();
    float getWetnessReductionRate();
    void notifyClientChanges();

}
