package com.unforbidable.tfc.bids.compat.tfc;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilPlan;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KilnRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.PartialMold;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

public class TfcRegistry {

    // Features register TFC recipes and values into registries below
    // and those are subsequently passed into TFC initializer from here

    public static class Recipes {

        public static final ListRegistry<RegistryActor<KnappingRecipe>> knapping = new ListRegistry<>();
        public static final ListRegistry<RegistryActor<KilnRecipe>> kiln = new ListRegistry<>();
        public static final ListRegistry<RegistryActor<AnvilPlan>> anvilPlans = new ListRegistry<>();
        public static final ListRegistry<RegistryActor<AnvilRecipe>> anvil = new ListRegistry<>();

    }

    public static class Values {

        public static final ListRegistry<RegistryActor<HeatValue>> heat = new ListRegistry<>();
        public static final ListRegistry<RegistryActor<PartialMold>> molds = new ListRegistry<>();

    }

}
