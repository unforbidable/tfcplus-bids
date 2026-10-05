package com.unforbidable.tfc.bids.features.material.glue;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class GlueConfig {

    public static boolean removeOriginalWaterSkinRecipes = false;

    public static void load(FeatureConfig config) {
        removeOriginalWaterSkinRecipes = config.getBoolean(
            "removeOriginalWaterSkinRecipes",
            removeOriginalWaterSkinRecipes,
            "Set this to true if you want to remove original TFC+ recipes for crafting a Water Skin.");
    }

}
