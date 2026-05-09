package com.unforbidable.tfc.bids.core.gui;

import com.unforbidable.tfc.bids.util.registry.MapRegistry;

public class GuiRegistry {

    // Guis are referenced by names, ids are used internally, purely dependent on order of registration.
    //
    // Registration is done as follows:
    // 1. Server/Client claims id and registers the id with given gui name
    // 2. Server/Client registers container provider for given gui name
    // 3. Client registers screen provider for given gui name
    //
    // When gui is to be opened, the id is simply looked up for given name

    public final static MapRegistry<String, Integer> guis = new MapRegistry<>();
    public final static MapRegistry<String, ContainerProvider<?, ?>> container = new MapRegistry<>();

    private static int nextAvailableId = 0;

    public static int getNextAvailableId() {
        return nextAvailableId++;
    }

}
