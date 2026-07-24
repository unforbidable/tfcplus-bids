package com.unforbidable.tfc.bids.core.features.setup.registry;

import com.unforbidable.tfc.bids.util.registry.Registry;
import java.util.function.Function;

public class RegistryAdapter<S, T> {

    public final Registry<S> source;
    public final Registry<T> target;
    public final Function<S, T> mapper;

    public RegistryAdapter(Registry<S> source, Registry<T> target, Function<S, T> mapper) {
        this.source = source;
        this.target = target;
        this.mapper = mapper;
    }

}
