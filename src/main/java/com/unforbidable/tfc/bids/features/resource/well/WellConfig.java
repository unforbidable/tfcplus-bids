package com.unforbidable.tfc.bids.features.resource.well;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class WellConfig {

    public static float aquiferChanceMultiplier = 1f;

    public static float aquiferSizeMultiplier = 1f;

    public static int aquiferMaxSurfaceHeight = 158;

    public static void load(FeatureConfig config) {
        aquiferChanceMultiplier = config.getFloat(
            "aquiferChanceMultiplier",
            aquiferChanceMultiplier, 0.25f, 4f,
            "Higher values increase the chance of an aquifer cluster generating in any given chunk. Value 1.0 roughly translates to 1 out of 8 chance.");
        aquiferSizeMultiplier = config.getFloat(
            "aquiferSizeMultiplier",
            aquiferSizeMultiplier, 1f, 4f,
            "Higher values increase the size of aquifer clusters. Aquifer clusters still generate smaller in drier areas than in wetter areas.");
        aquiferMaxSurfaceHeight = config.getInt(
            "aquiferMaxSurfaceHeight",
            aquiferMaxSurfaceHeight, 145, 195,
            "Sets the elevation limit for aquifer clusters to generate. The number corresponds to the elevation of the surface above rather than the aquifer itself.");
    }

}
