package com.unforbidable.tfc.bids.features.crafting.threshing;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class ThreshingConfig {

    public static boolean removeOriginalGrainRefiningRecipes = false;
    public static float threshingDurationMultiplier = 1f;

    public static void load(FeatureConfig config) {
        removeOriginalGrainRefiningRecipes = config.getBoolean(
            "removeOriginalGrainRefiningRecipes",
            removeOriginalGrainRefiningRecipes,
            "Set this to true if you want to remove recipes for refining grains using a knife in the crafting grid.");

        threshingDurationMultiplier = config.getFloat(
            "threshingDurationMultiplier",
            threshingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to thresh grains using a flail.");
    }

}
