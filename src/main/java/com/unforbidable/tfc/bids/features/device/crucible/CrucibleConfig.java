package com.unforbidable.tfc.bids.features.device.crucible;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class CrucibleConfig {

    public static boolean enableClayHandBreakable = true;
    public static boolean enableFireClayHandBreakable = false;
    public static boolean enableClassicHandBreakable = false;
    public static boolean enableOutputDisplay = false;
    public static boolean enableExactTemperatureDisplay = false;
    public static float solidHeatingMultiplier = 3;
    public static float liquidHeatingMultiplier = 6;
    public static float solidHeatingFromLiquidBonusMultiplier = 3;
    public static float coolingMultiplier = 1;
    public static float furnaceOverheatingRuinChance = 1f;

    public CrucibleConfig(FeatureConfig config) {
        enableClayHandBreakable = config.getBoolean("enableClayHandBreakable",
            enableClayHandBreakable,
            "Set this to true if you wish to be able to break a Clay Crucible by hand easily");
        enableFireClayHandBreakable = config.getBoolean("enableFireClayHandBreakable",
            enableFireClayHandBreakable,
            "Set this to true if you wish to be able to break a Fireclay Crucible by hand easily");
        enableClassicHandBreakable = config.getBoolean("enableClassicHandBreakable",
            enableClassicHandBreakable,
            "Set this to true if you wish to be able to break a classic TFC crucible by hand easily");
        enableOutputDisplay = config.getBoolean("enableOutputDisplay",
            enableOutputDisplay,
            "Set this to true if you wish to see the crucible output in the GUI");
        enableExactTemperatureDisplay = config.getBoolean("enableExactTemperatureDisplay",
            enableExactTemperatureDisplay,
            "Set this to true if you wish to see the exact temperature in the GUI (hover text)");
        solidHeatingMultiplier = config.getFloat("solidHeatingMultiplier",
            solidHeatingMultiplier, 1f, 10f,
            "Higher values increase the heat transfer to solid input materials from the heat source");
        solidHeatingFromLiquidBonusMultiplier = config.getFloat("solidHeatingFromLiquidBonusMultiplier",
            solidHeatingFromLiquidBonusMultiplier, 1f, 10f,
            "Higher values increase the heat transfer to solid input materials from molten metal that is already in the crucible");
        liquidHeatingMultiplier = config.getFloat("liquidHeatingMultiplier",
            liquidHeatingMultiplier, 1f, 10f,
            "Higher values increase the heat transfer to molten metal that is already in the crucible from the heat source");
        coolingMultiplier = config.getFloat("coolingMultiplier",
            coolingMultiplier, 1f, 10f,
            "Higher values increase the heat transfer rate when cooling down (heat loss)");
        furnaceOverheatingRuinChance = config.getFloat("furnaceOverheatingRuinChance",
            furnaceOverheatingRuinChance, 0f, 1f,
            "The chance of clay crucible to get ruined after making glass (0 = never, 1 = always)");
    }

}
