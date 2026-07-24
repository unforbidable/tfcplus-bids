package com.unforbidable.tfc.bids.core.gui;

import com.unforbidable.tfc.bids.core.gui.provider.GuiProvider;
import net.minecraft.inventory.Container;

public class ContainerProvider<T, G extends Container> {

    public final int id;
    public final GuiProvider<T, G> provider;

    public ContainerProvider(int id, GuiProvider<T, G> provider) {
        this.id = id;
        this.provider = provider;
    }

}
