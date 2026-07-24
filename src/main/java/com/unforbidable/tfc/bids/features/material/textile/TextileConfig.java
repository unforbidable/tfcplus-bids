package com.unforbidable.tfc.bids.features.material.textile;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class TextileConfig {

    public static boolean removeOriginalSpindleSpinningRecipes = false;
    public static boolean removeOriginalRopeMakingRecipes = false;
    public static boolean preventRopeMakingByRightClickingFibers = false;
    public static boolean removeOriginalBurlapFiberLoomRecipes = false;
    public static boolean enableCottonBollAutoConversion = false;

    public static void load(FeatureConfig config) {
        removeOriginalSpindleSpinningRecipes = config.getBoolean(
            "removeOriginalSpindleSpinningRecipes",
            removeOriginalSpindleSpinningRecipes,
            "Set this to true if you want to remove recipes for crafting strings and yarns with spindle in the crafting grid.");
        preventRopeMakingByRightClickingFibers = config.getBoolean(
            "preventRopeMakingByRightClickingFibers",
            preventRopeMakingByRightClickingFibers,
            "Set this to true if you want to prevent players from making ropes by right clicking fibers in hand.");
        removeOriginalRopeMakingRecipes = config.getBoolean(
            "removeOriginalRopeMakingRecipes",
            removeOriginalRopeMakingRecipes,
            "Set this to true if you want to remove recipes for crafting ropes from fibers in crafting grid.");
        removeOriginalBurlapFiberLoomRecipes = config.getBoolean(
            "removeOriginalBurlapFiberLoomRecipes",
            removeOriginalBurlapFiberLoomRecipes,
            "Set this to true if you want to remove recipes for crafting burlap directly from sisal and jute fibers on a Loom. Burlap can still be crafted from sisal and jute twine.");
        enableCottonBollAutoConversion = config.getBoolean(
            "enableCottonBollAutoConversion",
            enableCottonBollAutoConversion,
            "Set this to true if you want to automatically convert harvested cotton boll when picked up.");
    }

}
