package com.unforbidable.tfc.bids.compat.tfc.registry;

import com.unforbidable.tfc.bids.Bids;

import java.util.function.Predicate;

public abstract class RegistryStage<T> {

    public void add(T recipe) {
        Bids.LOG.warn("No action defined for adding recipe on stage {}", getClass());
    }

    public void remove(Predicate<T> predicate) {
        Bids.LOG.warn("No action defined for removing recipe on stage {}", getClass());
    }

}
