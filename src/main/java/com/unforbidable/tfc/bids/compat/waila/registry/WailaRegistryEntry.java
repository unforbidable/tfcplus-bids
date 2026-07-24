package com.unforbidable.tfc.bids.compat.waila.registry;

public class WailaRegistryEntry<T> {

    public final T provider;
    public final Class<?>[] types;

    public WailaRegistryEntry(T provider, Class<?> ...types) {
        this.provider = provider;
        this.types = types;
    }

}
