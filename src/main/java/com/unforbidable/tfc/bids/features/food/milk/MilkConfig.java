package com.unforbidable.tfc.bids.features.food.milk;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class MilkConfig {

    public static boolean enableGoatMilkFromGoats = true;
    public static boolean enableIbexHavingMilk = true;
    public static float ibexMilkingTimerMultiplier = 4f;

    public static boolean enableDefaultMilkingInteractionOverride = false;
    public static int milkingTimerReductionHours = 4;

    public static void load(FeatureConfig config) {
        enableGoatMilkFromGoats = config.getBoolean("enableGoatMilkFromGoats",
            enableGoatMilkFromGoats,
            "Set this to true if you want to get goat milk from Goats instead of regular milk.");
        enableIbexHavingMilk = config.getBoolean("enableIbexHavingMilk",
            enableIbexHavingMilk,
            "Set this to true if you want to be able to milk Ibex for a very limited amount of milk.");
        ibexMilkingTimerMultiplier = config.getFloat("ibexMilkingTimerMultiplier",
            ibexMilkingTimerMultiplier, 1f, 4f,
            "Higher values increase the milking timer for Ibex. This value can further prolong the already extended timer for milking Goats which is 5 days. For example, setting this value to 4 sets the Ibex milking timer to 20 days.");
        enableDefaultMilkingInteractionOverride = config.getBoolean("enableDefaultMilkingInteractionOverride",
            enableDefaultMilkingInteractionOverride,
            "Set this to true if you want to override the default milking interaction which means new mechanics are used even when milking using TFC buckets. If set to false, the new mechanics is used only when using large bowl or when milking an Ibex.");
        milkingTimerReductionHours = config.getInt("milkingTimerReductionHours",
            milkingTimerReductionHours, 0, 6,
            "Sets the number of hours by which the animal milking timer is reduced. For example, setting this value to 4 allows a cow to be milked every 20 hours which should allow milking daily on a loose schedule, 2 hours earlier or later.");
    }

}
