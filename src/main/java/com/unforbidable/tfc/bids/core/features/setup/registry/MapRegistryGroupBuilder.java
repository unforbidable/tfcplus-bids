package com.unforbidable.tfc.bids.core.features.setup.registry;

import com.unforbidable.tfc.bids.util.registry.Entry;
import com.unforbidable.tfc.bids.util.registry.MapRegistry;

import java.util.ArrayList;
import java.util.List;

public class MapRegistryGroupBuilder<K, V> {

    private final MapRegistry<K, V> registry;
    private final List<Entry<K, V>> values = new ArrayList<>();

    public MapRegistryGroupBuilder(MapRegistry<K, V> registry) {
        this.registry = registry;
    }

    public MapRegistryGroupBuilder<K, V> add(K key, V value) {
        values.add(new Entry<>(key, value));

        return this;
    }

    public MapRegistryGroup<K, V> build() {
        return new MapRegistryGroup<>(registry, values);
    }

}
