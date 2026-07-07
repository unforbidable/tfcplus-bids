package com.unforbidable.tfc.bids.features.resource.crop;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class CropConfig {

    public static boolean enableCerealSeedAutoConversion = false;
    public static boolean enableHardySeedAutoConversion = false;
    public static boolean enableVariableCropGrowthSpeed = true;

    public static void load(FeatureConfig config) {

        enableCerealSeedAutoConversion = config.getBoolean(
            "enableCerealSeedAutoConversion",
            enableCerealSeedAutoConversion,
            "Set this to true if you want to automatically convert the seeds of eligible cereal crops to new seeds that allow winter cereal cultivation when picked up.");
        enableHardySeedAutoConversion = config.getBoolean(
            "enableHardySeedAutoConversion",
            enableHardySeedAutoConversion,
            "Set this to true if you want to automatically convert the seeds of hardy crops (ie. onion, cabbage, garlic and carrot) to new seeds that allow sowing in fall when picked up.");
        enableVariableCropGrowthSpeed = config.getBoolean(
            "enableVariableCropGrowthSpeed",
            enableVariableCropGrowthSpeed,
            "Set this to true if you want crops to take variable time to mature. Some crops will mature a few days sooner, some later.");

    }

}
