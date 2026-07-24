package com.unforbidable.tfc.bids.features.crafting.spinning;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class SpinningConfig {

    public static float spinningDurationMultiplier = 1f;

    public static void load(FeatureConfig config) {
        spinningDurationMultiplier = config.getFloat(
            "spinningDurationMultiplier",
            spinningDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to spin strings and yarns using a spindle.");

    }

}
