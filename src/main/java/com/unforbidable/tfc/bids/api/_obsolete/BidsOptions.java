package com.unforbidable.tfc.bids.api._obsolete;

public class BidsOptions {

    public static class Crafting {

        public static boolean craftingAddMissingLeatherRepairRecipes = true;

        public static boolean enableSmallAndMediumRawhideSewing = true;

        public static boolean enableProcessingSurfaceLeatherRackOverride = false;

        public static float spinningDurationMultiplier = 1f;

        public static boolean removeOriginalSpindleSpinningRecipes = false;

        public static float ropeMakingDurationMultiplier = 1f;

        public static boolean removeOriginalRopeMakingRecipes = false;
        public static boolean preventRopeMakingByRightClickingFibers = false;

        public static boolean removeOriginalBurlapFiberLoomRecipes = false;

        public static float cardingDurationMultiplier = 1f;

        public static float hecklingDurationMultiplier = 1f;

        public static float handworkDurationMultiplier = 1f;

        public static boolean enableCottonBollAutoConversion = false;

        public static float soakingDurationMultiplier = 2f;
        public static boolean enableDryingSurfaceMudBrickDryingOverride = false;

    }

    public static class Miscellaneous {

        public static int soapUsageRewardXP = 2;
        public static int soapUsageRewardCoolDown = 6;

    }

}
