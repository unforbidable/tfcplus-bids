package com.unforbidable.tfc.bids.core.features.setup.registry;

import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import java.util.List;

public class RegistryGroup<T> {

    public final ListRegistry<T> registry;
    public final List<T> values;
    public final List<RegistryAdapter<?, ?>> adapters;

    public RegistryGroup(ListRegistry<T> registry, List<T> values, List<RegistryAdapter<?, ?>> adapters) {
        this.registry = registry;
        this.values = values;
        this.adapters = adapters;
    }

}
