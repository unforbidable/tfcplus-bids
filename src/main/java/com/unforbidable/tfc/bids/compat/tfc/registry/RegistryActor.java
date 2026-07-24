package com.unforbidable.tfc.bids.compat.tfc.registry;

public abstract class RegistryActor<T> {

    protected final RegistryStage<T> stage;

    public RegistryActor(RegistryStage<T> stage) {
        this.stage = stage;
    }

    public abstract void act();

}
