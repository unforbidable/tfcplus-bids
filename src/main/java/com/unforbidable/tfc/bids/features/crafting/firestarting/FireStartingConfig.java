package com.unforbidable.tfc.bids.features.crafting.firestarting;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class FireStartingConfig {

    public static boolean replaceOriginalFirestarterRecipes = true;
    public static float fireStartingDurationMultiplier = 1f;
    public static float fireStartingHumidityImpact = 0.25f;
    public static float fireStartingChanceNoTinder = 0.2f;
    public static float fireStartingChancePoorTinder = 0.4f;
    public static float fireStartingChanceGoodTinder = 1.0f;
    public static float fireStartingChanceExcellentTinder = 1.5f;

    public static void load(FeatureConfig config) {
        replaceOriginalFirestarterRecipes = config.getBoolean(
            "replaceOriginalFirestarterRecipes",
            replaceOriginalFirestarterRecipes,
            "Set this to true if you want to replace and remove the crafting of TFC fire starting tools. When disabled, Fire Plow firestarter cannot be crafted.");
        fireStartingDurationMultiplier = config.getFloat(
            "fireStartingDurationMultiplier",
            fireStartingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to start fire using manual fire starting tools.");
        fireStartingHumidityImpact = config.getFloat(
            "fireStartingHumidityImpact",
            fireStartingHumidityImpact, 0.0f, 2f,
            "Higher values increase the impact of humidity on the fire starting chance of success. Set to 0 to ignore humidity when starting fire.");
        fireStartingChanceNoTinder = config.getFloat(
            "fireStartingChanceNoTinder",
            fireStartingChanceNoTinder, 0.0f, 2f,
            "Higher values increase the chance of successful fire starting without tinder. Set to 0 to remove the option to start fire without tinder.");
        fireStartingChancePoorTinder = config.getFloat(
            "fireStartingChancePoorTinder",
            fireStartingChancePoorTinder, 0.0f, 2f,
            "Higher values increase the chance of successful fire starting using poor tinder.");
        fireStartingChanceGoodTinder = config.getFloat(
            "fireStartingChanceGoodTinder",
            fireStartingChanceGoodTinder, 0.0f, 2f,
            "Higher values increase the chance of successful fire starting using good tinder.");
        fireStartingChanceExcellentTinder = config.getFloat(
            "fireStartingChanceExcellentTinder",
            fireStartingChanceExcellentTinder, 0.0f, 2f,
            "Higher values increase the chance of successful fire starting using excellent tinder.");
    }

}
