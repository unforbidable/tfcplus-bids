package com.unforbidable.tfc.bids.features.crafting.ropemaking;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class RopeMakingConfig {

    public static float ropeMakingDurationMultiplier = 1f;

    public static void load(FeatureConfig config) {
        ropeMakingDurationMultiplier = config.getFloat(
            "ropeMakingDurationMultiplier",
            ropeMakingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to twist rope using a primitive rope maker.");
    }

}
