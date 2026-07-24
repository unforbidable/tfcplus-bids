package com.unforbidable.tfc.bids.features.crafting.carding;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class CardingConfig {

    public static float cardingDurationMultiplier = 1f;

    public static void load(FeatureConfig config) {
        cardingDurationMultiplier = config.getFloat(
            "cardingDurationMultiplier",
            cardingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to card fibers using a thorn or metal card.");
    }

}
