package com.unforbidable.tfc.bids.features.player.butchery;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class ButcheryConfig {

    public static boolean enableMoreButcheringSkillPoints = true;

    public static void load(FeatureConfig config) {
        enableMoreButcheringSkillPoints = config.getBoolean(
            "enableMoreButcheringSkillPoints",
            enableMoreButcheringSkillPoints,
            "Set to true to raise the amount of points needed to max out Butchering skill to compensate for additional skill gain from skin processing");
    }

}
