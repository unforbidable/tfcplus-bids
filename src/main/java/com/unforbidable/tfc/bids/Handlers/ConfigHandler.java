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
                BidsOptions.Crucible.solidHeatingMultiplier, 0.1f, 100f,
                "Higher values increase the heat transfer to solid input materials from the heat source");
        BidsOptions.Crucible.solidHeatingMultiplierFromLiquidBonus = config.getFloat(
                "solidHeatingMultiplierFromLiquidBonus", "crucible",
                BidsOptions.Crucible.solidHeatingMultiplierFromLiquidBonus, 0.1f, 100f,
                "Higher values increase the heat transfer to solid input materials from molten metal that is already in the crucible");
        BidsOptions.Crucible.liquidHeatingMultiplier = config.getFloat(
                "liquidHeatingMultiplier", "crucible",
                BidsOptions.Crucible.liquidHeatingMultiplier, 0.1f, 100f,
                "Higher values increase the heat transfer to molten metal that is already in the crucible from the heat source");

        config.save();
    }

    public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
        if (event.modID == Tags.MOD_ID) {
            loadConfig();
        }
    }

}
