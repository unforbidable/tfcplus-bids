package com.unforbidable.tfc.bids.features.crafting.drying;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.features.crafting.drying.nei.DryingNeiHandler;

@FeatureName("drying")
public class Drying extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(DryingConfig::load, "crafting");
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new DryingNeiHandler());
    }

}
