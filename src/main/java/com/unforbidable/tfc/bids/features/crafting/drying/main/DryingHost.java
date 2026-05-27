package com.unforbidable.tfc.bids.features.crafting.drying.main;

import com.unforbidable.tfc.bids.api.features.drying.DryingRecipe;

public interface DryingHost {

    DryingItem[] getDryingStorage();
    DryingRecipe getDryingRecipe(DryingItem item);
    float getWetnessIncreaseRate();
    float getWetnessReductionRate();
    void notifyClientChanges();

}
