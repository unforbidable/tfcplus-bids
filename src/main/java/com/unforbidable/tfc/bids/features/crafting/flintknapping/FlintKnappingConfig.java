package com.unforbidable.tfc.bids.features.crafting.flintknapping;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class FlintKnappingConfig {

    public static boolean enableFlintKnappingAnyStone = false;

    public static void load(FeatureConfig config) {
        enableFlintKnappingAnyStone = config.getBoolean(
            "enableFlintKnappingAnyStone",
            enableFlintKnappingAnyStone,
            "Set this to true if you want to be able to use any rock type as a soft or hard hammer when knapping flint.");
    }

}
