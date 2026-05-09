package com.unforbidable.tfc.bids.compat.tfc.registry.actors;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class RegistryAddingActor<T> extends RegistryActor<T> {

    private final T recipe;

    public RegistryAddingActor(RegistryStage<T> stage, T recipe) {
        super(stage);

        this.recipe = recipe;
    }

    @Override
    public void act() {
        stage.add(recipe);
    }

}
