package com.unforbidable.tfc.bids.util.registry;

import java.util.function.BiPredicate;

public class KeyListRegistry<K, V> extends ListRegistry<V> implements KeyRegistry<K, V> {

    private final BiPredicate<K, V> biPredicate;

    public KeyListRegistry(BiPredicate<K, V> biPredicate) {
        this.biPredicate = biPredicate;
    }

    @Override
    public V get(K key) {
        return get(value -> biPredicate.test(key, value));
    }

}
