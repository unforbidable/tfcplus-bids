package com.unforbidable.tfc.bids.features.crafting.handwork;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class HandworkConfig {

    public static float handworkDurationMultiplier = 1f;

    public static void load(FeatureConfig config) {
        handworkDurationMultiplier = config.getFloat(
            "handworkDurationMultiplier",
            handworkDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to process items by hand by holding right-mouse button.");
    }

}
