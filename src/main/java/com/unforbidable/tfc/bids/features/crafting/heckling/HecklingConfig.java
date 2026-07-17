package com.unforbidable.tfc.bids.features.crafting.heckling;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class HecklingConfig {

    public static float hecklingDurationMultiplier = 1f;

    public static void load(FeatureConfig config) {
        hecklingDurationMultiplier = config.getFloat(
            "hecklingDurationMultiplier",
            hecklingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to heckle fibers using a bone heckle.");
    }

}
