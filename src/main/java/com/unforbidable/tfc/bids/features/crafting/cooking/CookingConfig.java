package com.unforbidable.tfc.bids.features.crafting.cooking;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class CookingConfig {

    public static boolean enableFoodPrepPlacementOverride = false;
    public static int forceYeastGrowthAfterDays = 1;
    public static float environmentalYeastGrowthBaseChance = 1f;

    public static void load(FeatureConfig config) {
        enableFoodPrepPlacementOverride = config.getBoolean(
            "enableFoodPrepCreationOverride",
            enableFoodPrepPlacementOverride,
            "Set this to true if you want to create Cooking Prep surface, instead of the TFC Food Prep surface, when right-clicking block with a knife.");
        forceYeastGrowthAfterDays = config.getInt(
            "forceYeastGrowthAfterDays",
            forceYeastGrowthAfterDays, -1, 9999,
            "Higher values extend the time period after which Yeast automatically grows on foodstuff. Set to -1 to disable automatic yeast growth.");
        environmentalYeastGrowthBaseChance = config.getFloat(
            "environmentalYeastGrowthBaseChance",
            environmentalYeastGrowthBaseChance, 0f, 2f,
            "Higher values increase the chance of yeast growing on foodstuff based on the environment.");
    }

}
