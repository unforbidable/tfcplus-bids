package com.unforbidable.tfc.bids.features.utility.compositetools;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class CompositeToolsConfig {

    public static boolean enableGrassCordageAsToolBinding = true;

    public static boolean removeOriginalStoneToolRecipes = false;


    public static void load(FeatureConfig config) {
        removeOriginalStoneToolRecipes = config.getBoolean(
            "removeOriginalStoneToolRecipes",
            removeOriginalStoneToolRecipes,
            "Set this to true if you want to remove recipes for crafting stone tools and weapons without binding.");

        enableGrassCordageAsToolBinding = config.getBoolean(
            "enableGrassCordageAsToolBinding",
            enableGrassCordageAsToolBinding,
            "Set this to true if you want to be able to use grass cordage as composite tool binding.");
    }

}
