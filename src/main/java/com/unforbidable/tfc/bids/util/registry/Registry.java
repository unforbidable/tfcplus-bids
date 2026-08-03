package com.unforbidable.tfc.bids.util.registry;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Registry<V> extends Iterable<V> {

    void add(V value);

    Stream<V> stream();

    V get(Predicate<V> predicate);

    boolean has(Predicate<V> predicate);

}
