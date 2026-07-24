package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;

public class FeatureContext {

    public final FeatureRegistryLookup lookup;

    public FeatureContext(FeatureRegistryLookup lookup) {
        this.lookup = lookup;
    }

}
