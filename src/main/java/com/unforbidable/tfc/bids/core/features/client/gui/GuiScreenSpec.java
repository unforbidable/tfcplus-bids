package com.unforbidable.tfc.bids.core.features.client.gui;

import com.unforbidable.tfc.bids.core.gui.provider.GuiProvider;
import com.unforbidable.tfc.bids.core.gui.provider.GuiProviderContext;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Function;

public class GuiScreenSpec<T, G extends GuiScreen> {

    public final String name;
    public final GuiProvider<T, G> provider;

    public GuiScreenSpec(String name, GuiProvider<T, G> provider) {
        this.name = name;
        this.provider = provider;
    }

}
