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

    public static class WoodPile {

        public static float seasoningDurationMultiplier = 5f;
        public static boolean rotateItems = true;

    }

    public static class Firepit {

        public static boolean allowFuelLogsTFC = false;
        public static boolean allowFuelCharcoal = true;
        public static boolean replaceFirepitTFC = false;

    }

    public static class Bark {

        public static float dropPeelingChance = 0.25f;
        public static float dropPeelingSeasonedChance = 0.75f;
        public static float dropSplittingChance = 0f;
        public static float dropSplittingSeasonedChance = 0.10f;

    }

    public static class StonePress {

        public static float efficiency = 0.8f;

    }

    public static class LightSources {

        public static float clayLampLightLevel = 0.8f;
        public static float clayLampOliveOilConsumption = 0.25f;

    }

    public static class WorldGen {

        public static float aquiferChanceMultiplier = 1f;

        public static float aquiferSizeMultiplier = 1f;

        public static int aquiferMaxSurfaceHeight = 158;

    }

    public static class Crafting {

        public static boolean craftingAddMissingLeatherRepairRecipes = true;

    }

}
