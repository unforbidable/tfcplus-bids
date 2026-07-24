package com.unforbidable.tfc.bids.util.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ListRegistry<T> implements Registry<T> {

    private final List<T> items = new ArrayList<>();

    @Override
    public void add(T value) {
        items.add(value);
    }

    @Override
    public Stream<T> stream() {
        return items.stream();
    }

    @Override
    public T get(Predicate<T> predicate) {
        return stream().filter(predicate)
            .findAny().orElse(null);
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

}
