package com.unforbidable.tfc.bids.core.features.setup.registry;

import com.unforbidable.tfc.bids.util.registry.ListRegistry;

import java.util.List;

public class RegistryGroup<T> {

    public final ListRegistry<T> registry;
    public final List<T> values;

    public RegistryGroup(ListRegistry<T> registry, List<T> values) {
        this.registry = registry;
        this.values = values;
    }

}
