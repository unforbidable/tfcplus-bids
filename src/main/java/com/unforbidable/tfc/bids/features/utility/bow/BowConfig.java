package com.unforbidable.tfc.bids.features.utility.bow;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class BowConfig {

    public static boolean removeOriginalCompositeBowRecipes = false;

    public static void load(FeatureConfig config) {
        removeOriginalCompositeBowRecipes = config.getBoolean(
            "removeOriginalCompositeBowRecipes",
            removeOriginalCompositeBowRecipes,
            "Set this to true if you want to remove original TFC+ recipes for crafting a Composite Bow.");
    }

}
