package com.unforbidable.tfc.bids.features.device.woodpile;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class WoodpileConfig {

    public static boolean enablePlacementUsingLogsTFC = false;
    public static float seasoningDurationMultiplier = 5f;
    public static boolean rotateItems = true;
    public static boolean enableFireSetting = true;
    public static float burnTimeMultiplier = 1f;
    public static float pitchYieldMultiplier = 1f;
    public static boolean allowPitchFromNonResinousWood = true;
    public static boolean allowCharcoalFromUnseasonedFirewood = false;

    public static void load(FeatureConfig config) {
        WoodpileConfig.enablePlacementUsingLogsTFC = config.getBoolean("enablePlacementUsingLogsTFC",
            WoodpileConfig.enablePlacementUsingLogsTFC,
            "Set this to true if you want TFC logs to place a Wood Pile instead of TFC Log Pile");
        WoodpileConfig.rotateItems = config.getBoolean("rotateItems",
            WoodpileConfig.rotateItems,
            "Set this to true if you want odd rows in the wood pile appear rotated");
        WoodpileConfig.seasoningDurationMultiplier = config.getFloat("seasoningDurationMultiplier",
            WoodpileConfig.seasoningDurationMultiplier, 1f, 100f,
            "Higher values increase the time it takes for wood to season in a wood pile");
        WoodpileConfig.enableFireSetting = config.getBoolean("enableFireSetting",
            WoodpileConfig.enableFireSetting,
            "Set this to true if you want burning wood piles to crack nearby raw stone and ore bocks, aka fire-setting");
        WoodpileConfig.burnTimeMultiplier = config.getFloat("burnTimeMultiplier",
            WoodpileConfig.burnTimeMultiplier, 0.25f, 2f,
            "Higher values increase the time fuel burns in a wood pile (also affects kiln fuel consumption)");
        WoodpileConfig.allowPitchFromNonResinousWood = config.getBoolean("allowPitchFromNonResinousWood",
            WoodpileConfig.allowPitchFromNonResinousWood,
            "Set this to true if you want be able to extract pitch in a charcoal pit from non-resinous wood");
        WoodpileConfig.pitchYieldMultiplier = config.getFloat("pitchYieldMultiplier",
            WoodpileConfig.pitchYieldMultiplier, 0.25f, 2f,
            "Higher values increase the amount of pitch extracted during the production of charcoal");
        WoodpileConfig.allowCharcoalFromUnseasonedFirewood = config.getBoolean("allowCharcoalFromUnseasonedFirewood",
            WoodpileConfig.allowCharcoalFromUnseasonedFirewood,
            "Set this to true if you want to be able to make charcoal and extract pitch from unseasoned firewood");
    }

}
