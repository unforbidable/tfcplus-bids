package com.unforbidable.tfc.bids.features.crafting.ropemaking;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.nei.RopeMakingNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("ropeMaking")
public class RopeMaking extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(RopeMakingConfig::load, "crafting");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new RopeMakingNeiHandler());
    }

}
