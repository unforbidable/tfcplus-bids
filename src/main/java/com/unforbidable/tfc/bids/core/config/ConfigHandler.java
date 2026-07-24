package com.unforbidable.tfc.bids.core.config;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfigLoader;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import java.io.File;
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

        // Core configuration would appear here

        // Features are configured
        FeatureConfigLoader.load(config);

        config.save();
    }

    public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
        if (Tags.MOD_ID.equals(event.modID)) {
            loadConfig();
        }
    }

}
