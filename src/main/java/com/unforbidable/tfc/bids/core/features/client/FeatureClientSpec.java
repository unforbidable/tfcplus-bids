package com.unforbidable.tfc.bids.core.features.client;

import com.unforbidable.tfc.bids.core.features.client.block.BlockClientSpec;
import com.unforbidable.tfc.bids.core.features.client.eventhandler.EventHandlerClientSpec;
import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.client.item.ItemClientSpec;
import com.unforbidable.tfc.bids.core.features.client.tileentity.TileEntityClientSpec;

import java.util.List;

public class FeatureClientSpec {

    public final List<BlockClientSpec> blocks;
    public final List<ItemClientSpec> items;
    public final List<TileEntityClientSpec> tileEntities;
    public final List<GuiScreenSpec<?, ?>> screens;
    public final List<EventHandlerClientSpec> handlers;

    public FeatureClientSpec(List<BlockClientSpec> blocks, List<ItemClientSpec> items,
                             List<TileEntityClientSpec> tileEntities, List<GuiScreenSpec<?, ?>> screens,
                             List<EventHandlerClientSpec> handlers) {
        this.blocks = blocks;
        this.items = items;
        this.tileEntities = tileEntities;
        this.screens = screens;
        this.handlers = handlers;
    }

}
