package com.unforbidable.tfc.bids.api._obsolete.Registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapRegistryBase<K, V, E extends Entry<K, V>> implements Iterable<E> {

    private final Map<String, E> values = new HashMap<>();

    protected void register(K key, E entry) {
        register(getName(key), entry);
    }

    protected void register(String name, E entry) {
        values.put(name, entry);
    }

    public boolean has(K key) {
        return has(getName(key));
    }

    protected boolean has(String name) {
        return values.containsKey(name);
    }

    public V get(K key) {
        return get(getName(key));
    }

    protected V get(String name) {
        E entry = values.get(name);
        return entry != null ? entry.value : null;
    }

    protected String getName(K key) {
        return key.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return values.values().iterator();
    }

}
