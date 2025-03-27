package com.unforbidable.tfc.bids.api;

public class BidsOptions {

    public static class Crucible {

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

    }

    public static class Quarry {

        public static float bowStringBreakChance = 0.25f;
        public static int baseDrillDuration = 25;
        public static boolean enableDrillAutoRepair = true;

    }

    public static class Carving {

        public static boolean enableCarvingAnyBit = true;

    }

    public static class WoodPile {

        public static float seasoningDurationMultiplier = 5f;
        public static boolean rotateItems = true;
        public static boolean enableFireSetting = true;
        public static float burnTimeMultiplier = 1f;
        public static float pitchYieldMultiplier = 1f;
        public static boolean allowPitchFromNonResinousWood = true;
        public static boolean allowCharcoalFromUnseasonedFirewood = false;

    }

    public static class Firepit {

        public static boolean allowFuelLogsTFC = false;
        public static boolean allowFuelCharcoal = true;
        public static boolean allowFuelUnseasonedFirewood = false;
        public static boolean replaceFirepitTFC = false;
        public static float burnTimeMultiplier = 1f;

    }

    public static class Kiln {

        public static boolean enableTunnelKiln = true;
        public static boolean enableSquareKiln = true;
        public static boolean enableBeehiveKiln = true;
        public static boolean enableClimbingKiln = true;
        public static int maxTunnelKilnHeight = 2;
        public static int maxSquareKilnHeight = 2;
        public static int maxClimbingKilnHeight = 3;

    }

    public static class Bark {

        public static float dropPeelingChance = 0.25f;
        public static float dropPeelingSeasonedChance = 0.75f;
        public static float dropSplittingChance = 0f;
        public static float dropSplittingSeasonedChance = 0.10f;

    }

    public static class SaddleQuern {

        public static boolean allowGrindHematite = false;
        public static boolean allowGrindLimonite = false;
        public static boolean allowGrindMalachite = true;
        public static boolean allowGrindLapisLazuli = false;

    }

    public static class StonePress {

        public static float efficiency = 0.8f;

    }

    public static class ScrewPress {

        public static float efficiency = 1.1f;

    }

    public static class Churning {

        public static float churningDurationMultiplier = 2f;

    }

    public static class LightSources {

        public static float clayLampOliveOilLightLevel = 0.8f;
        public static float clayLampOliveOilConsumption = 0.25f;
        public static float clayLampFishOilLightLevel = 0.65f;
        public static float clayLampFishOilConsumption = 0.20f;

    }

    public static class WorldGen {

        public static float aquiferChanceMultiplier = 1f;

        public static float aquiferSizeMultiplier = 1f;

        public static int aquiferMaxSurfaceHeight = 158;

    }

    public static class Crafting {

        public static boolean craftingAddMissingLeatherRepairRecipes = true;

        public static boolean enableSmallAndMediumRawhideSewing = true;

        public static boolean enableProcessingSurfaceLeatherRackOverride = false;

    }

    public static class Crops {

        public static boolean enableCerealSeedAutoConversion = false;
        public static boolean enableHardySeedAutoConversion = false;
        public static boolean enableVariableCropGrowthSpeed = true;

    }

    public static class Husbandry {

        public static boolean enableGoatMilkFromGoats = true;
        public static boolean enableIbexHavingMilk = true;
        public static float ibexMilkingTimerMultiplier = 4f;

        public static boolean enableDefaultMilkingInteractionOverride = false;
        public static int milkingTimerReductionHours = 4;

    }

}
