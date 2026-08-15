package com.unforbidable.tfc.bids.features.resource.flora;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class FloraConfig {

    public static float fernChanceMultiplier = 1f;

    public static float fernBaseSizeMultiplier = 1f;

    public static void load(FeatureConfig config) {
        fernChanceMultiplier = config.getFloat(
            "fernChanceMultiplier",
            fernChanceMultiplier, 0.25f, 4f,
            "Higher values increase the chance of an fern cluster generating in any given chunk. Value 1.0 roughly translates to 1 out of 16 chance.");
        fernBaseSizeMultiplier = config.getFloat(
            "fernBaseSizeMultiplier",
            fernBaseSizeMultiplier, 0.5f, 2f,
            "Higher values increase the size of fern clusters. Fern clusters still generate a little smaller in drier areas than in wetter areas.");
    }

}
