package com.unforbidable.tfc.bids.features.crafting.spinning;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.features.crafting.spinning.nei.SpinningNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("spinning")
public class Spinning extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(SpinningConfig::load, "crafting");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new SpinningNeiHandler());
    }

}
