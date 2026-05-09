package com.unforbidable.tfc.bids.core.features.init.gui;

import com.unforbidable.tfc.bids.core.gui.provider.GuiProvider;
import net.minecraft.inventory.Container;

public class GuiContainerSpec<T, G extends Container> {

    public final String name;
    public final GuiProvider<T, G> provider;

    public GuiContainerSpec(String name, GuiProvider<T, G> provider) {
        this.name = name;
        this.provider = provider;
    }

}
