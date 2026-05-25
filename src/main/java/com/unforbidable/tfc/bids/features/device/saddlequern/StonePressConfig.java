package com.unforbidable.tfc.bids.features.device.saddlequern;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class StonePressConfig {

    public static float efficiency = 0.8f;

    public static void load(FeatureConfig config) {
        efficiency = config.getFloat("efficiency",
            efficiency, 0.5f, 1.5f,
            "Higher values increase the efficiency of a stone press, 1.0f being equal to the classic TFC hopper press.");
    }

}
