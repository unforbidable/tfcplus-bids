package com.unforbidable.tfc.bids.features.material.skin;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class SkinConfig {

    public static boolean enableAnimalSkinDropReplacement = true;

    public static void load(FeatureConfig config) {
        enableAnimalSkinDropReplacement = config.getBoolean(
            "enableAnimalSkinDropReplacement",
            enableAnimalSkinDropReplacement,
            "Set this to true if you want to replace dropped rawhide and fur with higher-yield animal skins that decay and require more processing.");
    }

}
