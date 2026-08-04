package com.unforbidable.tfc.bids.features.crafting.cooking;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class CookingConfig {

    public static boolean enableFoodPrepPlacementOverride = false;

    public static void load(FeatureConfig config) {
        enableFoodPrepPlacementOverride = config.getBoolean(
            "enableFoodPrepCreationOverride",
            enableFoodPrepPlacementOverride,
            "Set this to true if you want to create Cooking Prep surface, instead of the TFC Food Prep surface, when right-clicking block with a knife.");
    }

}
