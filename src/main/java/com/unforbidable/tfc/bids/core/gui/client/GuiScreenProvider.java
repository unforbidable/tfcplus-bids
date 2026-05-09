package com.unforbidable.tfc.bids.core.gui.client;

import com.unforbidable.tfc.bids.core.gui.provider.GuiProvider;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenProvider<T, S extends GuiScreen> {

    public final int id;
    public final GuiProvider<T, S> provider;

    public GuiScreenProvider(int id, GuiProvider<T, S> provider) {
        this.id = id;
        this.provider = provider;
    }

}
