package com.unforbidable.tfc.bids.compat.tfc;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

public class TfcRegistryHelper {

    private static boolean worldBoundRecipesRegistered = false;

    public static void registerCommon() {
        Bids.LOG.info("Register TFC recipes");
        register(TfcRegistry.Recipes.knapping);
        register(TfcRegistry.Recipes.kiln);
        register(TfcRegistry.Recipes.barrel);
        register(TfcRegistry.Recipes.loom);

        Bids.LOG.info("Register TFC values");
        register(TfcRegistry.Values.heat);
        register(TfcRegistry.Values.molds);
    }

    public static void registerWorldLoad() {
        if (!worldBoundRecipesRegistered) {
            Bids.LOG.info("Register TFC world-bound recipes");

            register(TfcRegistry.Recipes.anvilPlans);
            register(TfcRegistry.Recipes.anvil);
            register(TfcRegistry.Recipes.sewing);

            worldBoundRecipesRegistered = true;
        }
    }

    private static <T> void register(ListRegistry<RegistryActor<T>> registry) {
        registry.stream()
            .forEach(RegistryActor::act);
    }

}
