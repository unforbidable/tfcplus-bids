package com.unforbidable.tfc.bids.core.features.setup.registry;

import com.unforbidable.tfc.bids.util.registry.Entry;
import com.unforbidable.tfc.bids.util.registry.MapRegistry;

import java.util.List;

public class MapRegistryGroup<K, V> {

    public final MapRegistry<K, V> registry;
    public final List<Entry<K, V>> values;

    public MapRegistryGroup(MapRegistry<K, V> registry, List<Entry<K, V>> values) {
        this.registry = registry;
        this.values = values;
    }

}
