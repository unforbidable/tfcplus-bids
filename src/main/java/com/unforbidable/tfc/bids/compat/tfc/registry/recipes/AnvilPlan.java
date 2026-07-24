package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;

public class AnvilPlan {

    public final String name;
    public final String[] rules;

    public AnvilPlan(String name, String[] rules) {
        this.name = name;
        this.rules = rules;
    }

    public static RegistryAddingActor<AnvilPlan> add(String name, String ...rules) {
        return new RegistryAddingActor<>(AnvilPlanStage.instance, new AnvilPlan(name, rules));
    }

}
