package com.unforbidable.tfc.bids.features.device.soakingsurface.main;

import com.unforbidable.tfc.bids.api.features.soaking.SoakingSurfaceRecipe;

public class SoakingSurfaceSlotProgress {


    public final SoakingSurfaceRecipe recipe;
    public final float progress;
    public final float hoursRemaining;

    public SoakingSurfaceSlotProgress(SoakingSurfaceRecipe recipe, float progress, float hoursRemaining) {
        this.recipe = recipe;
        this.progress = progress;
        this.hoursRemaining = hoursRemaining;
    }

}
