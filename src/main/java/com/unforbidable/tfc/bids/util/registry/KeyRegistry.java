package com.unforbidable.tfc.bids.util.registry;

public interface KeyRegistry<K, V> {

    void add(K key, V value);

    V get(K key);

}
