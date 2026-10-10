package com.unforbidable.tfc.bids.features.device.saddlequern;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class SaddleQuernConfig {

    public static boolean allowGrindAny = false;

    public static void load(FeatureConfig config) {
        allowGrindAny = config.getBoolean("allowGrindAny",
            allowGrindAny,
            "Set this to true to be able to grind Small Hematite, Limonite, Malachite and Lapis Lazuli into Dyes using Saddle Quern");
    }

}
