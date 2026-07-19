package com.unforbidable.tfc.bids.compat.tfc.registry.actors;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;
import java.util.function.Predicate;

public class RegistryRemovingActor<T> extends RegistryActor<T> {

    private final Predicate<T> predicate;

    public RegistryRemovingActor(RegistryStage<T> stage, Predicate<T> predicate) {
        super(stage);

        this.predicate = predicate;
    }

    @Override
    public void act() {
        stage.remove(predicate);
    }

}
