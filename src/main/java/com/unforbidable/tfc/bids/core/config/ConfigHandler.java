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

        BidsOptions.Crafting.enableProcessingSurfaceLeatherRackOverride = config.getBoolean(
            "enableProcessingSurfaceLeatherRackOverride", "crafting",
            BidsOptions.Crafting.enableProcessingSurfaceLeatherRackOverride,
            "Set this to true if you want to scrap leathers using the Processing Surface, instead of the TFC Leather Rack.");

        BidsOptions.Crafting.removeOriginalSpindleSpinningRecipes = config.getBoolean(
            "removeOriginalSpindleSpinningRecipes", "crafting",
            BidsOptions.Crafting.removeOriginalSpindleSpinningRecipes,
            "Set this to true if you want to remove recipes for crafting strings and yarns with spindle in the crafting grid.");
        BidsOptions.Crafting.spinningDurationMultiplier = config.getFloat(
            "spinningDurationMultiplier", "crafting",
            BidsOptions.Crafting.spinningDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to spin strings and yarns using a spindle.");

        BidsOptions.Crafting.preventRopeMakingByRightClickingFibers = config.getBoolean(
            "preventRopeMakingByRightClickingFibers", "crafting",
            BidsOptions.Crafting.preventRopeMakingByRightClickingFibers,
            "Set this to true if you want to prevent players from making ropes by right clicking fibers in hand.");
        BidsOptions.Crafting.removeOriginalRopeMakingRecipes = config.getBoolean(
            "removeOriginalRopeMakingRecipes", "crafting",
            BidsOptions.Crafting.removeOriginalRopeMakingRecipes,
            "Set this to true if you want to remove recipes for crafting ropes from fibers in crafting grid.");
        BidsOptions.Crafting.ropeMakingDurationMultiplier = config.getFloat(
            "ropeMakingDurationMultiplier", "crafting",
            BidsOptions.Crafting.ropeMakingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to twist rope using a primitive rope maker.");

        BidsOptions.Crafting.removeOriginalBurlapFiberLoomRecipes = config.getBoolean(
            "removeOriginalBurlapFiberLoomRecipes", "crafting",
            BidsOptions.Crafting.removeOriginalBurlapFiberLoomRecipes,
            "Set this to true if you want to remove recipes for crafting burlap directly from sisal and jute fibers on a Loom. Burlap can still be crafted from sisal and jute twine.");

        BidsOptions.Crafting.cardingDurationMultiplier = config.getFloat(
            "cardingDurationMultiplier", "crafting",
            BidsOptions.Crafting.cardingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to card fibers using a thorn or metal card.");

        BidsOptions.Crafting.hecklingDurationMultiplier = config.getFloat(
            "hecklingDurationMultiplier", "crafting",
            BidsOptions.Crafting.hecklingDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to heckle fibers using a bone heckle.");

        BidsOptions.Crafting.handworkDurationMultiplier = config.getFloat(
            "handworkDurationMultiplier", "crafting",
            BidsOptions.Crafting.handworkDurationMultiplier, 0.5f, 4f,
            "Higher values increase the time it takes to process items by hand by holding right-mouse button.");

        BidsOptions.Crafting.enableCottonBollAutoConversion = config.getBoolean(
            "enableCottonBollAutoConversion", "crafting",
            BidsOptions.Crafting.enableCottonBollAutoConversion,
            "Set this to true if you want to automatically convert harvested cotton boll when picked up.");

        BidsOptions.Crafting.soakingDurationMultiplier = config.getFloat(
            "soakingDurationMultiplier", "crafting",
            BidsOptions.Crafting.soakingDurationMultiplier, 0.5f, 10f,
            "Higher values increase the time it takes to soak stuff on a Soaking Surface");
        BidsOptions.Crafting.enableDryingSurfaceMudBrickDryingOverride = config.getBoolean(
            "enableDryingSurfaceMudBrickDryingOverride", "crafting", BidsOptions.Crafting.enableDryingSurfaceMudBrickDryingOverride,
            "Set this to true if you want to dry mud bricks using the Drying Surface, instead of the TFC mechanics.");

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
