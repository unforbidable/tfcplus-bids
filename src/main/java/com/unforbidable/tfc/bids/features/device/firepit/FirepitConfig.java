package com.unforbidable.tfc.bids.features.device.firepit;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class FirepitConfig {

    public static boolean allowFuelLogsTFC = false;
    public static boolean allowFuelCharcoal = true;
    public static boolean allowFuelUnseasonedFirewood = false;
    public static boolean replaceFirepitTFC = false;
    public static float burnTimeMultiplier = 1f;
    public static boolean allowAshRemovalAlsoFromFirepitTFC = true;
    public static boolean requireKindlingToLight = true;

    public static void load(FeatureConfig config) {
        allowFuelLogsTFC = config.getBoolean(
            "allowFuelLogsTFC",
            allowFuelLogsTFC,
            "Set this to true if you want to be able to use unseasoned TFC logs as fuel in a firepit");
        allowFuelCharcoal = config.getBoolean(
            "allowFuelCharcoal",
            allowFuelCharcoal,
            "Set this to true if you want to be able to use charcoal as fuel in a firepit");
        allowFuelUnseasonedFirewood = config.getBoolean(
            "allowFuelUnseasonedFirewood",
            allowFuelUnseasonedFirewood,
            "Set this to true if you want to be able to use unseasoned Firewood as fuel in a firepit");
        replaceFirepitTFC = config.getBoolean(
            "replaceFirepitTFC",
            replaceFirepitTFC,
            "Set this to true if you want to replace TFC firepit");
        burnTimeMultiplier = config.getFloat(
            "burnTimeMultiplier",
            burnTimeMultiplier, 0.25f, 2f,
            "Higher values increase the time fuel burns in a firepit");
        allowAshRemovalAlsoFromFirepitTFC = config.getBoolean(
            "allowAshRemovalAlsoFromFirepitTFC",
            allowAshRemovalAlsoFromFirepitTFC,
            "Set this to true if you want to be able to extract ash from TFC firepit much like from the new firepit when you choose not to replace it");
        requireKindlingToLight = config.getBoolean(
            "requireKindlingToLight",
            requireKindlingToLight,
            "Set this to true to make firepit require kindling (eg. sticks and stick bundles) to light");
    }

}
