package com.unforbidable.tfc.bids.api._obsolete;

public class BidsOptions {

    public static class Kiln {

        public static boolean enableTunnelKiln = true;
        public static boolean enableSquareKiln = true;
        public static boolean enableBeehiveKiln = true;
        public static boolean enableClimbingKiln = true;
        public static int maxTunnelKilnHeight = 2;
        public static int maxSquareKilnHeight = 2;
        public static int maxClimbingKilnHeight = 3;

    }

    public static class ScrewPress {

        public static float efficiency = 1.1f;

    }

    public static class Churning {

        public static float churningDurationMultiplier = 2f;

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

    public static class Miscellaneous {

        public static int soapUsageRewardXP = 2;
        public static int soapUsageRewardCoolDown = 6;

    }

}
