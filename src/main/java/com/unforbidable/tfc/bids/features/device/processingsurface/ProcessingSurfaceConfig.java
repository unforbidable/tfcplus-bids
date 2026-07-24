package com.unforbidable.tfc.bids.features.device.processingsurface;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class ProcessingSurfaceConfig {

    public static boolean enableProcessingSurfaceLeatherRackOverride = false;

    public static void load(FeatureConfig config) {
        enableProcessingSurfaceLeatherRackOverride = config.getBoolean(
            "enableProcessingSurfaceLeatherRackOverride",
            enableProcessingSurfaceLeatherRackOverride,
            "Set this to true if you want to scrap leathers using the Processing Surface, instead of the TFC Leather Rack.");
    }

}
