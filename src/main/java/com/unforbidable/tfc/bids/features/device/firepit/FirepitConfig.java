package com.unforbidable.tfc.bids.features.device.firepit;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class FirepitConfig {

    public static boolean allowFuelLogsTFC = false;
    public static boolean allowFuelCharcoal = true;
    public static boolean allowFuelUnseasonedFirewood = false;
    public static boolean replaceFirepitTFC = false;
    public static float burnTimeMultiplier = 1f;
    public static boolean allowAshRemovalAlsoFromFirepitTFC = true;

    public static void load(FeatureConfig config) {
        FirepitConfig.allowFuelLogsTFC = config.getBoolean(
            "allowFuelLogsTFC", FirepitConfig.allowFuelLogsTFC,
            "Set this to true if you want to be able to use unseasoned TFC logs as fuel in a firepit");
        FirepitConfig.allowFuelCharcoal = config.getBoolean(
            "allowFuelCharcoal", FirepitConfig.allowFuelCharcoal,
            "Set this to true if you want to be able to use charcoal as fuel in a firepit");
        FirepitConfig.allowFuelUnseasonedFirewood = config.getBoolean(
            "allowFuelUnseasonedFirewood",  FirepitConfig.allowFuelUnseasonedFirewood,
            "Set this to true if you want to be able to use unseasoned Firewood as fuel in a firepit");
        FirepitConfig.replaceFirepitTFC = config.getBoolean(
            "replaceFirepitTFC", FirepitConfig.replaceFirepitTFC,
            "Set this to true if you want to replace TFC firepit");
        FirepitConfig.burnTimeMultiplier = config.getFloat(
            "burnTimeMultiplier", FirepitConfig.burnTimeMultiplier, 0.25f, 2f,
            "Higher values increase the time fuel burns in a firepit");
        FirepitConfig.allowAshRemovalAlsoFromFirepitTFC = config.getBoolean(
            "allowAshRemovalAlsoFromFirepitTFC", FirepitConfig.allowAshRemovalAlsoFromFirepitTFC,
            "Set this to true if you want to be able to extract ash from TFC firepit much like from the new firepit when you choose not to replace it");
    }

}
