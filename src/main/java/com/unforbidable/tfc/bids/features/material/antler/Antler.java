package com.unforbidable.tfc.bids.features.material.antler;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonMisc;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.material.antler.eventhandler.AntlerLivingDropsEventHandler;

@FeatureName("antler")
public class Antler extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.ANTLER, ItemCommonMisc::new)
            .meta("Deer");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new AntlerLivingDropsEventHandler());

        setup.ores("materialAntler")
            .add(BidsItems.antler);
    }

}
