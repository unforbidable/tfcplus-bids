package com.unforbidable.tfc.bids.compat.tfc;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilPlan;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.BarrelRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KilnRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.LoomRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.SewingRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.PartialMold;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

public class TfcRegistry {

    // Features register TFC recipes and values into registries below
    // and those are subsequently passed into TFC initializer from here

    public static class Knapping {
        public static final ListRegistry<RegistryActor<KnappingRecipe>> recipes = new ListRegistry<>();
    }

    public static class Kiln {
        public static final ListRegistry<RegistryActor<KilnRecipe>> recipes = new ListRegistry<>();
    }

    public static class Anvil {
        public static final ListRegistry<RegistryActor<AnvilPlan>> plans = new ListRegistry<>();
        public static final ListRegistry<RegistryActor<AnvilRecipe>> recipes = new ListRegistry<>();
    }

    public static class Barrel {
        public static final ListRegistry<RegistryActor<BarrelRecipe>> recipes = new ListRegistry<>();
    }

    public static class Sewing {
        public static final ListRegistry<RegistryActor<SewingRecipe>> recipes = new ListRegistry<>();
    }

    public static class Loom {
        public static final ListRegistry<RegistryActor<LoomRecipe>> recipes = new ListRegistry<>();
    }

    public static class Heat {
        public static final ListRegistry<RegistryActor<HeatValue>> values = new ListRegistry<>();
    }

    public static class Metal {
        public static final ListRegistry<RegistryActor<PartialMold>> molds = new ListRegistry<>();
    }

}
