package com.unforbidable.tfc.bids.features.device.dryingsurface;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class DryingSurfaceConfig {

    public static boolean enableDryingSurfaceMudBrickDryingOverride = false;

    public static void load(FeatureConfig config) {
        enableDryingSurfaceMudBrickDryingOverride = config.getBoolean(
            "enableDryingSurfaceMudBrickDryingOverride",
            enableDryingSurfaceMudBrickDryingOverride,
            "Set this to true if you want to dry mud bricks using the Drying Surface, instead of the TFC mechanics.");
    }

}
