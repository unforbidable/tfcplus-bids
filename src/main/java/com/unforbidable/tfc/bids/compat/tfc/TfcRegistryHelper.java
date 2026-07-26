package com.unforbidable.tfc.bids.compat.tfc;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

public class TfcRegistryHelper {

    private static boolean worldBoundRecipesRegistered = false;

    public static void registerCommon() {
        Bids.LOG.info("Register TFC recipes");
        register(TfcRegistry.Knapping.recipes);
        register(TfcRegistry.Kiln.recipes);
        register(TfcRegistry.Barrel.recipes);
        register(TfcRegistry.Loom.recipes);

        Bids.LOG.info("Register TFC values");
        register(TfcRegistry.Heat.values);
        register(TfcRegistry.Metal.molds);
    }

    public static void registerWorldLoad() {
        if (!worldBoundRecipesRegistered) {
            Bids.LOG.info("Register TFC world-bound recipes");

            register(TfcRegistry.Anvil.plans);
            register(TfcRegistry.Anvil.recipes);
            register(TfcRegistry.Sewing.recipes);

            worldBoundRecipesRegistered = true;
        }
    }

    private static <T> void register(ListRegistry<RegistryActor<T>> registry) {
        registry.stream()
            .forEach(RegistryActor::act);
    }

}
