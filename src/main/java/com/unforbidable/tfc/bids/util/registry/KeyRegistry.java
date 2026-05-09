package com.unforbidable.tfc.bids.util.registry;

public interface KeyRegistry<K, V> {

    V get(K key);

}
