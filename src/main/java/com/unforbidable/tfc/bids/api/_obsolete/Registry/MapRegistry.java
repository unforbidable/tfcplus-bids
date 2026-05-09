package com.unforbidable.tfc.bids.api._obsolete.Registry;

public class MapRegistry<K, V> extends MapRegistryBase<K, V, Entry<K, V>> {

    public void register(K key, V value) {
        super.register(key, new Entry<>(key, value));
    }

}
