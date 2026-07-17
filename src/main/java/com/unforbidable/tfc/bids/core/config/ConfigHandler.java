package com.unforbidable.tfc.bids.core.config;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfigLoader;
import com.unforbidable.tfc.bids.features.device.kiln.KilnConfig;
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

        // TODO move to specific feature

        BidsOptions.Crafting.craftingAddMissingLeatherRepairRecipes = config.getBoolean(
            "craftingAddMissingLeatherRepairRecipes", "crafting",
            BidsOptions.Crafting.craftingAddMissingLeatherRepairRecipes,
            "Set this to true if you want to add missing leather armor repair recipes.");

        BidsOptions.Miscellaneous.soapUsageRewardXP = config.getInt(
            "soapUsageRewardXP", "miscellaneous",
            BidsOptions.Miscellaneous.soapUsageRewardXP, 0, 4,
            "Sets the amount of XP awarded for using soap.");
        BidsOptions.Miscellaneous.soapUsageRewardCoolDown = config.getInt(
            "soapUsageRewardCoolDown", "miscellaneous",
            BidsOptions.Miscellaneous.soapUsageRewardCoolDown, 0, 24,
            "Sets the number of hours needed to pass for the player to receive an XP reward for using soap again after receiving an XP reward.");

        config.save();
    }

    public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
        if (Tags.MOD_ID.equals(event.modID)) {
            loadConfig();
        }
    }

}
