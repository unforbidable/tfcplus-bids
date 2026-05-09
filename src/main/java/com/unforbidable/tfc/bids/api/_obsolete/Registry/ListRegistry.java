package com.unforbidable.tfc.bids.api._obsolete.Registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ListRegistry<V> implements Iterable<V> {

    public final String name;

    private final List<V> values = new ArrayList<>();

    public ListRegistry(String name) {
        this.name = name;
    }

    public void register(V value) {
        values.add(value);
    }

    public Stream<V> stream() {
        return values.stream();
    }

    @Override
    public Iterator<V> iterator() {
        return values.iterator();
    }

}
