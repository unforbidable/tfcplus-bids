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
        BidsOptions.Crucible.furnaceOverheatingRuinChance = config.getFloat(
                "furnaceOverheatingRuinChance", "crucible",
                BidsOptions.Crucible.furnaceOverheatingRuinChance, 0f, 1f,
                "The chance of clay crucible to get ruined after making glass (0 = never, 1 = always)");

        BidsOptions.Quarry.baseDrillDuration = config.getInt(
                "baseDrillDuration", "quarry",
                BidsOptions.Quarry.baseDrillDuration, 10, 1000,
                "Higher values increase the time it takes to drill a hole");
        BidsOptions.Quarry.bowStringBreakChance = config.getFloat(
                "bowStringBreakChance", "quarry",
                BidsOptions.Quarry.bowStringBreakChance, 0f, 1f,
                "The chance of bow string breaking when a drill breaks (0 = never, 1 = always)");
        BidsOptions.Quarry.enableDrillAutoRepair = config.getBoolean(
                "enableDrillAutoRepair", "quarry", BidsOptions.Quarry.enableDrillAutoRepair,
                "Set this to true if you want to see stone drills automatically repaired using material from the hotbar");

        BidsOptions.Carving.enableCarvingAnyBit = config.getBoolean(
            "enableCarvingAnyBit", "carving", BidsOptions.Carving.enableCarvingAnyBit,
            "Set this to true if you want to be able to carve any bit with an Adze; if set to false, chimney blocks cannot be carved");

        BidsOptions.WoodPile.rotateItems = config.getBoolean(
                "rotateItems", "woodpile", BidsOptions.WoodPile.rotateItems,
                "Set this to true if you want odd rows in the wood pile appear rotated");
        BidsOptions.WoodPile.seasoningDurationMultiplier = config.getFloat(
                "seasoningDurationMultiplier", "woodpile", BidsOptions.WoodPile.seasoningDurationMultiplier, 1f, 100f,
                "Higher values increase the time it takes for wood to season in a wood pile");
        BidsOptions.WoodPile.enableFireSetting = config.getBoolean(
            "enableFireSetting", "woodpile", BidsOptions.WoodPile.enableFireSetting,
            "Set this to true if you want burning wood piles to crack nearby raw stone and ore bocks, aka fire-setting");
        BidsOptions.WoodPile.burnTimeMultiplier = config.getFloat(
            "burnTimeMultiplier", "woodpile", BidsOptions.WoodPile.burnTimeMultiplier, 0.25f, 2f,
            "Higher values increase the time fuel burns in a wood pile (also affects kiln fuel consumption)");
        BidsOptions.WoodPile.pitchResinousWoodOnly = config.getBoolean(
            "pitchResinousWoodOnly", "woodpile", BidsOptions.WoodPile.pitchResinousWoodOnly,
            "Set this to true if you want only resinous wood to produce pitch in a charcoal pit");
        BidsOptions.WoodPile.pitchYieldMultiplier = config.getFloat(
            "pitchYieldMultiplier", "woodpile", BidsOptions.WoodPile.pitchYieldMultiplier, 0.25f, 2f,
            "Higher values increase the amount of pitch extracted during the production of charcoal");

        BidsOptions.Firepit.allowFuelLogsTFC = config.getBoolean(
                "allowFuelLogsTFC", "firepit", BidsOptions.Firepit.allowFuelLogsTFC,
                "Set this to true if you want to be able to use unseasoned TFC logs as fuel in a firepit");
        BidsOptions.Firepit.allowFuelCharcoal = config.getBoolean(
                "allowFuelCharcoal", "firepit", BidsOptions.Firepit.allowFuelCharcoal,
                "Set this to true if you want to be able to use charcoal as fuel in a firepit");
        BidsOptions.Firepit.replaceFirepitTFC = config.getBoolean(
                "replaceFirepitTFC", "firepit", BidsOptions.Firepit.replaceFirepitTFC,
                "Set this to true if you want to replace TFC firepit");
        BidsOptions.Firepit.burnTimeMultiplier = config.getFloat(
            "burnTimeMultiplier", "firepit", BidsOptions.Firepit.burnTimeMultiplier, 0.25f, 2f,
            "Higher values increase the time fuel burns in a firepit");

        BidsOptions.Kiln.enableBeehiveKiln = config.getBoolean(
            "enableBeehiveKiln", "kiln", BidsOptions.Kiln.enableBeehiveKiln,
            "Set this to true if you want to add Beehive kiln as an available kiln structure");
        BidsOptions.Kiln.enableTunnelKiln = config.getBoolean(
            "enableTunnelKiln", "kiln", BidsOptions.Kiln.enableTunnelKiln,
            "Set this to true if you want to add Tunnel kiln as an available kiln structure");
        BidsOptions.Kiln.enableSquareKiln = config.getBoolean(
            "enableSquareKiln", "kiln", BidsOptions.Kiln.enableSquareKiln,
            "Set this to true if you want to add Square kiln as an available kiln structure");
        BidsOptions.Kiln.enableClimbingKiln = config.getBoolean(
            "enableClimbingKiln", "kiln", BidsOptions.Kiln.enableClimbingKiln,
            "Set this to true if you want to add Climbing kiln as an available kiln structure");
        BidsOptions.Kiln.maxTunnelKilnHeight = config.getInt(
            "maxTunnelKilnHeight", "kiln",
            BidsOptions.Kiln.maxTunnelKilnHeight, 1, 2,
            "Maximum allowed height of the Tunnel kiln chamber; setting this to 2 allows the Tunnel kiln to be walked in");
        BidsOptions.Kiln.maxSquareKilnHeight = config.getInt(
            "maxSquareKilnHeight", "kiln",
            BidsOptions.Kiln.maxSquareKilnHeight, 1, 2,
            "Maximum allowed height of the Square kiln chamber; setting this to 2 allows the Square kiln to be walked in");
        BidsOptions.Kiln.maxClimbingKilnHeight = config.getInt(
            "maxClimbingKilnHeight", "kiln",
            BidsOptions.Kiln.maxClimbingKilnHeight, 1, 3,
            "Maximum allowed height of the Climbing kiln chamber; one unit of height corresponds to one section adding 6 more pottery slots on top of the initial 6");

        BidsOptions.Bark.dropPeelingChance = config.getFloat(
                "dropPeelingChance", "bark",
                BidsOptions.Bark.dropPeelingChance, 0f, 1f,
                "The chance of a bark piece dropping when peeling unseasoned logs (0 = never, 1 = always)");
        BidsOptions.Bark.dropPeelingSeasonedChance = config.getFloat(
                "dropPeelingSeasonedChance", "bark",
                BidsOptions.Bark.dropPeelingSeasonedChance, 0f, 1f,
                "The chance of a bark piece dropping when peeling seasoned logs (0 = never, 1 = always)");
        BidsOptions.Bark.dropSplittingChance = config.getFloat(
                "dropSplittingChance", "bark",
                BidsOptions.Bark.dropSplittingChance, 0f, 1f,
                "The chance of a bark piece dropping when splitting unseasoned logs into firewood (0 = never, 1 = always)");
        BidsOptions.Bark.dropSplittingSeasonedChance = config.getFloat(
                "dropSplittingSeasonedChance", "bark",
                BidsOptions.Bark.dropSplittingSeasonedChance, 0f, 1f,
                "The chance of a bark piece dropping when splitting seasoned logs into firewood (0 = never, 1 = always)");

        BidsOptions.SaddleQuern.allowGrindHematite = config.getBoolean(
            "allowGrindHematite", "saddleQuern", BidsOptions.SaddleQuern.allowGrindHematite,
            "Set this to true to be able to grind Small Hematite ore using Saddle Quern"
        );
        BidsOptions.SaddleQuern.allowGrindLimonite = config.getBoolean(
            "allowGrindLimonite", "saddleQuern", BidsOptions.SaddleQuern.allowGrindLimonite,
            "Set this to true to be able to grind Small Limonite ore using Saddle Quern"
        );
        BidsOptions.SaddleQuern.allowGrindMalachite = config.getBoolean(
            "allowGrindMalachite", "saddleQuern", BidsOptions.SaddleQuern.allowGrindMalachite,
            "Set this to true to be able to grind Small Malachite ore using Saddle Quern"
        );
        BidsOptions.SaddleQuern.allowGrindLapisLazuli = config.getBoolean(
            "allowGrindLapisLazuli", "saddleQuern", BidsOptions.SaddleQuern.allowGrindLapisLazuli,
            "Set this to true to be able to grind Lapis Lazuli using Saddle Quern"
        );

        BidsOptions.StonePress.efficiency = config.getFloat(
                "efficiency", "stonePress",
                BidsOptions.StonePress.efficiency, 0.5f, 1.5f,
                "Higher values increase the efficiency of a stone press, 1.0f being equal to the classic TFC hopper press.");

        BidsOptions.ScrewPress.efficiency = config.getFloat(
                "efficiency", "screwPress",
                BidsOptions.ScrewPress.efficiency, 0.5f, 1.5f,
                "Higher values increase the efficiency of a screw press, 1.0f being equal to the classic TFC hopper press.");

        BidsOptions.LightSources.clayLampOliveOilLightLevel = config.getFloat(
                "clayLampOliveOilLightLevel", "lightSources",
                BidsOptions.LightSources.clayLampOliveOilLightLevel, 0.1f, 1f,
                "Higher values increase the light level of clay lamps consuming olive oil, 1f being equal to the light level of TFC metal oil lamps.");
        BidsOptions.LightSources.clayLampOliveOilConsumption = config.getFloat(
                "clayLampOliveOilConsumption", "lightSources",
                BidsOptions.LightSources.clayLampOliveOilConsumption, 0.1f, 10f,
                "Higher values increase the olive oil consumption in clay lamps, 0.125f being equal to the olive oil consumption of TFC metal oil lamps.");
        BidsOptions.LightSources.clayLampFishOilLightLevel = config.getFloat(
                "clayLampFishOilLightLevel", "lightSources",
                BidsOptions.LightSources.clayLampFishOilLightLevel, 0.1f, 1f,
                "Higher values increase the light level of clay lamps consuming fish oil, 1f being equal to the light level of TFC metal oil lamps.");
        BidsOptions.LightSources.clayLampFishOilConsumption = config.getFloat(
                "clayLampFishOilConsumption", "lightSources",
                BidsOptions.LightSources.clayLampFishOilConsumption, 0.1f, 10f,
                "Higher values increase the fish oil consumption in clay lamps, 0.125f being equal to the olive oil consumption of TFC metal oil lamps.");

        BidsOptions.WorldGen.aquiferChanceMultiplier = config.getFloat(
                "aquiferChanceMultiplier", "worldGen",
                BidsOptions.WorldGen.aquiferChanceMultiplier, 0.25f, 4f,
                "Higher values increase the chance of an aquifer cluster generating in any given chunk. Value 1.0 roughly translates to 1 out of 8 chance.");
        BidsOptions.WorldGen.aquiferSizeMultiplier = config.getFloat(
                "aquiferSizeMultiplier", "worldGen",
                BidsOptions.WorldGen.aquiferSizeMultiplier, 1f, 4f,
                "Higher values increase the size of aquifer clusters. Aquifer clusters still generate smaller in drier areas than in wetter areas.");
        BidsOptions.WorldGen.aquiferMaxSurfaceHeight = config.getInt(
                "aquiferMaxSurfaceHeight", "worldGen",
                BidsOptions.WorldGen.aquiferMaxSurfaceHeight, 145, 195,
                "Sets the elevation limit for aquifer clusters to generate. The number corresponds to the elevation of the surface above rather than the aquifer itself.");

        BidsOptions.Crafting.craftingAddMissingLeatherRepairRecipes = config.getBoolean(
            "craftingAddMissingLeatherRepairRecipes", "crafting",
            BidsOptions.Crafting.craftingAddMissingLeatherRepairRecipes,
            "Set this to true if you want to add missing leather armor repair recipes.");

        BidsOptions.Crops.enableCerealSeedAutoConversion = config.getBoolean(
            "enableCerealSeedAutoConversion", "crops",
            BidsOptions.Crops.enableCerealSeedAutoConversion,
            "Set this to true if you want to automatically convert the seeds of eligible cereal crops to new seeds that allow winter cereal cultivation when picked up.");
        BidsOptions.Crops.enableHardySeedAutoConversion = config.getBoolean(
            "enableHardySeedAutoConversion", "crops",
            BidsOptions.Crops.enableHardySeedAutoConversion,
            "Set this to true if you want to automatically convert the seeds of hardy crops (ie. onion, cabbage, garlic and carrot) to new seeds that allow sowing in fall when picked up.");
        BidsOptions.Crops.enableVariableCropGrowthSpeed = config.getBoolean(
            "enableVariableCropGrowthSpeed", "crops",
            BidsOptions.Crops.enableVariableCropGrowthSpeed,
            "Set this to true if you want crops to take variable time to mature. Some crops will mature a few days sooner, some later.");

        config.save();
    }

    public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
        if (Tags.MOD_ID.equals(event.modID)) {
            loadConfig();
        }
    }

}
