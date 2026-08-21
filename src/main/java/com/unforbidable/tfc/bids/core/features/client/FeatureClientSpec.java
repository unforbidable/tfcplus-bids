package com.unforbidable.tfc.bids.core.features.client;

import com.unforbidable.tfc.bids.core.features.client.eventhandler.EventHandlerClientSpec;
import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderBlockSpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderEntitySpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderItemSpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderTileEntitySpec;
import java.util.List;

public class FeatureClientSpec {

    public final List<RenderBlockSpec> blocks;
    public final List<RenderItemSpec> items;
    public final List<RenderTileEntitySpec> tileEntities;
    public final List<RenderEntitySpec> entities;
    public final List<GuiScreenSpec<?, ?>> screens;
    public final List<Runnable> runs;
    public final List<EventHandlerClientSpec> handlers;

    public FeatureClientSpec(List<RenderBlockSpec> blocks, List<RenderItemSpec> items,
                             List<RenderTileEntitySpec> tileEntities, List<RenderEntitySpec> entities, List<GuiScreenSpec<?, ?>> screens,
                             List<Runnable> runs, List<EventHandlerClientSpec> handlers) {
        this.blocks = blocks;
        this.items = items;
        this.tileEntities = tileEntities;
        this.entities = entities;
        this.screens = screens;
        this.runs = runs;
        this.handlers = handlers;
    }

}
