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

}
