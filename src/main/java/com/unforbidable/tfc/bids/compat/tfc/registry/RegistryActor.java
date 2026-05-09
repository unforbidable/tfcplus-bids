package com.unforbidable.tfc.bids.compat.tfc.registry;

public abstract class RegistryActor<S> {

    protected final RegistryStage<S> stage;

    public RegistryActor(RegistryStage<S> stage) {
        this.stage = stage;
    }

    public abstract void act();

}
