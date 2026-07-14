package com.unforbidable.tfc.bids.features.crafting.drying;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.drying.WetnessInfo;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.drying.nei.DryingNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("drying")
public class Drying extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(DryingConfig::load, "crafting");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new DryingNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(DryingRegistry.wetness)
//            .add(BidsItems.barkFibre, new WetnessInfo(500, 1f))
//            .add(BidsItems.sisalFiberRinsed, new WetnessInfo(500, 1f))
            .add(TFCItems.juteFiber, new WetnessInfo(500, 1f))
//            .add(BidsItems.flaxStalk, new WetnessInfo(1000, 1f))
//            .add(BidsItems.flaxStalkRetted, new WetnessInfo(500, 0.5f))
//            .add(BidsItems.woolRinsed, new WetnessInfo(1000, 1f))
            .add(TFCItems.seaWeed, new WetnessInfo(500, 1f));
    }

}
