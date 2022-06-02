package com.unforbidable.tfc.bids.Handlers;

import java.io.File;

import com.unforbidable.tfc.bids.Bids;
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
        Bids.LOG.info("Config loaded");

        BidsOptions.enableCrucibleOutputDisplay = config.getBoolean("enableCrucibleOutputDisplay", "general",
                BidsOptions.enableCrucibleOutputDisplay,
                "Set this to true if you wish to see the crucible output in the GUI");

        if (config.hasChanged()) {
            config.save();
            Bids.LOG.info("Config saved");
        }
    }

    public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
        if (event.modID == Tags.MOD_ID) {
            loadConfig();
        }
    }

}
