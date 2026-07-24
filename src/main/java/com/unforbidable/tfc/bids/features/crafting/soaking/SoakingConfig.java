package com.unforbidable.tfc.bids.features.crafting.soaking;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class SoakingConfig {

    public static float soakingDurationMultiplier = 2f;

    public static void load(FeatureConfig config) {
        soakingDurationMultiplier = config.getFloat(
            "soakingDurationMultiplier",
            soakingDurationMultiplier, 0.5f, 10f,
            "Higher values increase the time it takes to soak stuff on a Soaking Surface");
    }

}
