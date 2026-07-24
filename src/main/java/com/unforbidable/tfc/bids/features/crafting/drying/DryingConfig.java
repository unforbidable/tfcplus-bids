package com.unforbidable.tfc.bids.features.crafting.drying;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class DryingConfig {

    public static float dryingDurationMultiplier = 1f;
    public static float smokingDurationMultiplier = 1f;

    public static void load(FeatureConfig config) {
        dryingDurationMultiplier = config.getFloat(
            "dryingDurationMultiplier",
            dryingDurationMultiplier, 0.5f, 10f,
            "Higher values increase the time it takes to dry stuff, but also ret plants and similar, on Drying Rack or Drying Surface");
        smokingDurationMultiplier = config.getFloat(
            "smokingDurationMultiplier",
            smokingDurationMultiplier, 0.5f, 10f,
            "Higher values increase the time it takes to smoke stuff, typically food, on Drying Rack");
    }

}
