package com.unforbidable.tfc.bids.features.resource.straw;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class StrawConfig {

    public static float tallGrassBlockHardness = 0;
    public static float cropBlockHardness = 3;

    public static void load(FeatureConfig config) {
        tallGrassBlockHardness = config.getFloat(
            "tallGrassBlockHardness",
            tallGrassBlockHardness, 0, 8,
            "Higher values increase the hardness of TFC Tall Grass blocks. Set to 0 to retain original TFC value.");
        cropBlockHardness = config.getFloat(
            "cropBlockHardness",
            cropBlockHardness, 0, 8,
            "Higher values increase the hardness of TFC Crop blocks. Set to 0 to retain original TFC value.");
    }

}
