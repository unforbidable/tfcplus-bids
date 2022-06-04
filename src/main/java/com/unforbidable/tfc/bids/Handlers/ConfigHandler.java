package com.unforbidable.tfc.bids.Handlers;

import java.io.File;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsOptions;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private final Configuration config;

    public ConfigHandler(File configDir) {
        File configFile = new File(configDir.toString() + "/" + Tags.MOD_ID + ".cfg");
        config = new Configuration(configFile);

        loadConfig();
    }

    private void loadConfig() {
        config.load();

        BidsOptions.Crucible.enableClayHandBreakable = config.getBoolean(
                "enableClayHandBreakable", "crucible",
                BidsOptions.Crucible.enableClayHandBreakable,
                "Set this to true if you wish to be able to break a Clay Crucible by hand easily");
        BidsOptions.Crucible.enableFireClayHandBreakable = config.getBoolean(
                "enableFireClayHandBreakable", "crucible",
                BidsOptions.Crucible.enableFireClayHandBreakable,
                "Set this to true if you wish to be able to break a Fireclay Crucible by hand easily");
        BidsOptions.Crucible.enableClassicHandBreakable = config.getBoolean(
                "enableClassicHandBreakable", "crucible",
                BidsOptions.Crucible.enableClassicHandBreakable,
                "Set this to true if you wish to be able to break a classic TFC crucible by hand easily");
        BidsOptions.Crucible.enableOutputDisplay = config.getBoolean(
                "enableOutputDisplay", "crucible",
                BidsOptions.Crucible.enableOutputDisplay,
                "Set this to true if you wish to see the crucible output in the GUI");
        BidsOptions.Crucible.enableExactTemperatureDisplay = config.getBoolean(
                "enableExactTemperatureDisplay", "crucible",
                BidsOptions.Crucible.enableExactTemperatureDisplay,
                "Set this to true if you wish to see the exact temperature in the GUI (hover text)");
        BidsOptions.Crucible.solidHeatingMultiplier = config.getFloat(
                "solidHeatingMultiplier", "crucible",
                BidsOptions.Crucible.solidHeatingMultiplier, 1f, 10f,
                "Higher values increase the heat transfer to solid input materials from the heat source");
        BidsOptions.Crucible.solidHeatingFromLiquidBonusMultiplier = config.getFloat(
                "solidHeatingFromLiquidBonusMultiplier", "crucible",
                BidsOptions.Crucible.solidHeatingFromLiquidBonusMultiplier, 1f, 10f,
                "Higher values increase the heat transfer to solid input materials from molten metal that is already in the crucible");
        BidsOptions.Crucible.liquidHeatingMultiplier = config.getFloat(
                "liquidHeatingMultiplier", "crucible",
                BidsOptions.Crucible.liquidHeatingMultiplier, 1f, 10f,
                "Higher values increase the heat transfer to molten metal that is already in the crucible from the heat source");
        BidsOptions.Crucible.coolingMultiplier = config.getFloat(
                "coolingMultiplier", "crucible",
                BidsOptions.Crucible.coolingMultiplier, 1f, 10f,
                "Higher values increase the heat transfer rate when cooling down (heat loss)");

        config.save();
    }

    public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
        if (event.modID == Tags.MOD_ID) {
            loadConfig();
        }
    }

}
