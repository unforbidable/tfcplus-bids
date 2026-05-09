package com.unforbidable.tfc.bids.compat.waila.registry;

import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaEntityProvider;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

public class WailaRegistry {

    public static final ListRegistry<WailaRegistryEntry<WailaDataProvider>> dataProviders = new ListRegistry<>();
    public static final ListRegistry<WailaRegistryEntry<WailaEntityProvider>> entityProviders = new ListRegistry<>();

}
