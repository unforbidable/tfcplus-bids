package com.unforbidable.tfc.bids.compat.tfc.registry.actors;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class RegistryCloningActor<T> extends RegistryActor<T> {

    private final Predicate<T> predicate;
    private final List<Function<T, T>> mappers = new ArrayList<>();

    public RegistryCloningActor(RegistryStage<T> stage, Predicate<T> predicate) {
        super(stage);
        this.predicate = predicate;
    }

    @Override
    public void act() {
        stage.clone(predicate, mappers);
    }

    public RegistryActor<T> as(Function<T, T> mapper) {
        mappers.add(mapper);

        return this;
    }

}
