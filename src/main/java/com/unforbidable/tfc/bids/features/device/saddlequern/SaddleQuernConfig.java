package com.unforbidable.tfc.bids.features.device.saddlequern;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class SaddleQuernConfig {

    public static boolean allowGrindHematite = false;
    public static boolean allowGrindLimonite = false;
    public static boolean allowGrindMalachite = true;
    public static boolean allowGrindLapisLazuli = false;

    public static void load(FeatureConfig config) {
        allowGrindHematite = config.getBoolean("allowGrindHematite",
            allowGrindHematite,
            "Set this to true to be able to grind Small Hematite ore using Saddle Quern");
        allowGrindLimonite = config.getBoolean("allowGrindLimonite",
            allowGrindLimonite,
            "Set this to true to be able to grind Small Limonite ore using Saddle Quern");
        allowGrindMalachite = config.getBoolean("allowGrindMalachite",
            allowGrindMalachite,
            "Set this to true to be able to grind Small Malachite ore using Saddle Quern");
        allowGrindLapisLazuli = config.getBoolean("allowGrindLapisLazuli",
            allowGrindLapisLazuli,
            "Set this to true to be able to grind Lapis Lazuli using Saddle Quern");
    }

}
