package com.unforbidable.tfc.bids.features.crafting.quern;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.features.crafting.quern.nei.SaddleQuernNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@FeatureName("quern")
public class Quern extends Feature {

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new SaddleQuernNeiHandler());
    }

}
