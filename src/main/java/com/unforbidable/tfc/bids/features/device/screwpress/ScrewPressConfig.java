package com.unforbidable.tfc.bids.features.device.screwpress;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class ScrewPressConfig {

    public static float efficiency = 1.1f;

    public static void load(FeatureConfig config) {
        ScrewPressConfig.efficiency = config.getFloat(
            "efficiency",
            ScrewPressConfig.efficiency, 0.5f, 1.5f,
            "Higher values increase the efficiency of a screw press, 1.0f being equal to the classic TFC hopper press.");
    }

}
