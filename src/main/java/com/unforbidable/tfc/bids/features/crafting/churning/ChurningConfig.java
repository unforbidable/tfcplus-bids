package com.unforbidable.tfc.bids.features.crafting.churning;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class ChurningConfig {

    public static float churningDurationMultiplier = 2f;

    public static void load(FeatureConfig config) {
        ChurningConfig.churningDurationMultiplier = config.getFloat(
            "churningDurationMultiplier",
            ChurningConfig.churningDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to churn butter");
    }

}
