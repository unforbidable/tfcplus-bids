package com.unforbidable.tfc.bids.features.resource.butchery;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.SkillsManager;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;

@FeatureName("butchery")
public class Butchery extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(ButcheryConfig::load);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Increase butchering skill max points
        // because skin processing now gives additional points
        // TODO Start with 300 but more might be appropriate
        if (ButcheryConfig.enableMoreButcheringSkillPoints) {
            setup.run(() -> {
                Bids.LOG.info("Changing the Butchering skill rate to 300 points");
                SkillsManager.instance.getSkill(Global.SKILL_BUTCHERING).skillRate = 300;
            });
        }
    }

}
