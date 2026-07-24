package com.unforbidable.tfc.bids.features.material.bark;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class BarkConfig {

    public static float dropPeelingChance = 0.25f;
    public static float dropPeelingSeasonedChance = 0.75f;
    public static float dropSplittingChance = 0f;
    public static float dropSplittingSeasonedChance = 0.10f;

    public static void load(FeatureConfig config) {
        dropPeelingChance = config.getFloat(
            "dropPeelingChance",
            BarkConfig.dropPeelingChance, 0f, 1f,
            "The chance of a bark piece dropping when peeling unseasoned logs (0 = never, 1 = always)");
        dropPeelingSeasonedChance = config.getFloat(
            "dropPeelingSeasonedChance",
            BarkConfig.dropPeelingSeasonedChance, 0f, 1f,
            "The chance of a bark piece dropping when peeling seasoned logs (0 = never, 1 = always)");
        dropSplittingChance = config.getFloat(
            "dropSplittingChance",
            BarkConfig.dropSplittingChance, 0f, 1f,
            "The chance of a bark piece dropping when splitting unseasoned logs into firewood (0 = never, 1 = always)");
        dropSplittingSeasonedChance = config.getFloat(
            "dropSplittingSeasonedChance",
            BarkConfig.dropSplittingSeasonedChance, 0f, 1f,
            "The chance of a bark piece dropping when splitting seasoned logs into firewood (0 = never, 1 = always)");
    }

}
