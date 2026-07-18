package com.unforbidable.tfc.bids.features.material.soap;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class SoapConfig {

    public static int soapUsageRewardXP = 2;
    public static int soapUsageRewardCoolDown = 6;

    public static void load(FeatureConfig config) {
        SoapConfig.soapUsageRewardXP = config.getInt(
            "soapUsageRewardXP",
            SoapConfig.soapUsageRewardXP, 0, 4,
            "Sets the amount of XP awarded for using soap.");
        SoapConfig.soapUsageRewardCoolDown = config.getInt(
            "soapUsageRewardCoolDown",
            SoapConfig.soapUsageRewardCoolDown, 0, 24,
            "Sets the number of hours needed to pass for the player to receive an XP reward for using soap again after receiving an XP reward.");
    }

}
