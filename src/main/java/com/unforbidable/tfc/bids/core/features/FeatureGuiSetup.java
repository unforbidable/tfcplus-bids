package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.init.gui.GuiContainerSpec;
import com.unforbidable.tfc.bids.core.gui.ContainerProvider;
import com.unforbidable.tfc.bids.core.gui.GuiRegistry;
import com.unforbidable.tfc.bids.core.gui.client.ClientGuiRegistry;
import com.unforbidable.tfc.bids.core.gui.client.GuiScreenProvider;

public class FeatureGuiSetup {

    public static void registerGuiContainer(GuiContainerSpec<?, ?> container) {
        Bids.LOG.info("Register GUI container '{}'", container.name);

        int id = GuiRegistry.getNextAvailableId();
        GuiRegistry.guis.add(container.name, id);
        GuiRegistry.container.add(container.name, new ContainerProvider<>(id, container.provider));
    }

    public static void registerGuiScreen(GuiScreenSpec<?, ?> screen) {
        Bids.LOG.info("Register GUI screen '{}'", screen.name);

        Integer id = GuiRegistry.guis.get(screen.name);
        if (id != null) {
            ClientGuiRegistry.screens.add(screen.name, new GuiScreenProvider<>(id, screen.provider));
        } else {
            Bids.LOG.error("GUI container for '{}' must be registered first", screen.name);
        }
    }

}
