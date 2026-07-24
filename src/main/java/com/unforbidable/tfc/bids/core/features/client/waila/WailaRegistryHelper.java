package com.unforbidable.tfc.bids.core.features.client.waila;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaEntityProvider;
import com.unforbidable.tfc.bids.compat.waila.registry.WailaRegistry;
import com.unforbidable.tfc.bids.compat.waila.registry.WailaRegistryEntry;

public class WailaRegistryHelper {

    public WailaRegistryHelper data(WailaDataProvider provider, Class<?> ...tileEntities) {
        WailaRegistry.dataProviders.add(new WailaRegistryEntry<>(provider, tileEntities));

        return this;
    }

    public WailaRegistryHelper entity(WailaEntityProvider provider, Class<?> ...tileEntities) {
        WailaRegistry.entityProviders.add(new WailaRegistryEntry<>(provider, tileEntities));

        return this;
    }

}
