package com.unforbidable.tfc.bids.util.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MapRegistry<K, V> implements Registry<MapRegistry.Entry<K, V>>, KeyRegistry<K, V> {

    private final Map<K, Entry<K, V>> items = new HashMap<>();

    public void add(K key, V value) {
        add(new Entry<>(key, value));
    }

    @Override
    public void add(Entry<K, V> value) {
        items.put(value.key, value);
    }

    @Override
    public Stream<Entry<K, V>> stream() {
        return items.values().stream();
    }

    @Override
    public V get(K key) {
        return items.containsKey(key) ? items.get(key).value : null;
    }

    @Override
    public Entry<K, V> get(Predicate<Entry<K, V>> predicate) {
        return stream().filter(predicate)
            .findFirst().orElse(null);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return items.values().iterator();
    }

    public static class Entry<K, V> {

        public final K key;
        public final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

}
