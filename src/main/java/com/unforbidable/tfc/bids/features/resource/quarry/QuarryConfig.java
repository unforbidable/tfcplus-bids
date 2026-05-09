package com.unforbidable.tfc.bids.features.resource.quarry;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class QuarryConfig {

    public static float bowStringBreakChance = 0.25f;
    public static int baseDrillDuration = 25;
    public static boolean enableDrillAutoRepair = true;

    public QuarryConfig(FeatureConfig config) {
        baseDrillDuration = config.getInt("baseDrillDuration",
            baseDrillDuration, 10, 1000,
            "Higher values increase the time it takes to drill a hole");
        bowStringBreakChance = config.getFloat("bowStringBreakChance",
            bowStringBreakChance, 0f, 1f,
            "The chance of bow string breaking when a drill breaks (0 = never, 1 = always)");
        enableDrillAutoRepair = config.getBoolean("enableDrillAutoRepair",
            enableDrillAutoRepair,
            "Set this to true if you want to see stone drills automatically repaired using material from the hot bar");
    }

}
