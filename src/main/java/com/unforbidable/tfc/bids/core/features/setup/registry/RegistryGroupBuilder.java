package com.unforbidable.tfc.bids.core.features.setup.registry;

import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import com.unforbidable.tfc.bids.util.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RegistryGroupBuilder<T> {

    private final ListRegistry<T> registry;
    private final List<T> values = new ArrayList<>();
    private final List<RegistryAdapter<?, ?>> adapters = new ArrayList<>();

    public RegistryGroupBuilder(ListRegistry<T> registry) {
        this.registry = registry;
    }

    public RegistryGroupBuilder<T> add(T value) {
        values.add(value);

        return this;
    }

    public <S> RegistryGroupBuilder<T> adapt(Registry<S> source, Function<S, Optional<T>> mapper) {
        this.adapters.add(new RegistryAdapter<>(source, registry, mapper));

        return this;
    }

    public RegistryGroup<T> build() {
        return new RegistryGroup<>(registry, values, adapters);
    }

}
