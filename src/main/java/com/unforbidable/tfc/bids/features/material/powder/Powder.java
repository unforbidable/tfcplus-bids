package com.unforbidable.tfc.bids.features.material.powder;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.features.material.powder.item.ItemMorePowder;

import static com.unforbidable.tfc.bids.api.names.ItemNames.MORE_POWDER;

@FeatureName("powder")
public class Powder extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(MORE_POWDER, ItemMorePowder::new)
            .meta("Sawdust");
    }

}
