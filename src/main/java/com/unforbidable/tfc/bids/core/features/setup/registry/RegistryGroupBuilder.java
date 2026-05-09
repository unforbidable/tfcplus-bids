package com.unforbidable.tfc.bids.core.features.setup.registry;


import com.unforbidable.tfc.bids.util.registry.ListRegistry;

import java.util.ArrayList;
import java.util.List;

public class RegistryGroupBuilder<T> {

    public final ListRegistry<T> registry;
    public final List<T> values = new ArrayList<>();

    public RegistryGroupBuilder(ListRegistry<T> registry) {
        this.registry = registry;
    }

    public RegistryGroupBuilder<T> add(T value) {
        values.add(value);

        return this;
    }

    public RegistryGroup<T> build() {
        return new RegistryGroup<>(registry, values);
    }

}
