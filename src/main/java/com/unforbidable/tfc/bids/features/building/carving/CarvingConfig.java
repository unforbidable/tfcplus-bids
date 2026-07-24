package com.unforbidable.tfc.bids.features.building.carving;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class CarvingConfig {

    public static boolean enableCarvingAnyBit = true;

    public static void load(FeatureConfig config) {
        enableCarvingAnyBit = config.getBoolean("enableCarvingAnyBit",
            enableCarvingAnyBit,
            "Set this to true if you want to be able to carve any bit with an Adze; if set to false, chimney blocks cannot be carved");
    }

}
