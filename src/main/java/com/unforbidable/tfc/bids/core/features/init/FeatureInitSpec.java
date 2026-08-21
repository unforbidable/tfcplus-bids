package com.unforbidable.tfc.bids.core.features.init;

import com.unforbidable.tfc.bids.core.features.init.block.BlockSpec;
import com.unforbidable.tfc.bids.core.features.init.entity.EntitySpec;
import com.unforbidable.tfc.bids.core.features.init.fluid.FluidSpec;
import com.unforbidable.tfc.bids.core.features.init.gui.GuiContainerSpec;
import com.unforbidable.tfc.bids.core.features.init.item.ItemSpec;
import com.unforbidable.tfc.bids.core.features.init.tileentity.TileEntitySpec;
import java.util.List;

public class FeatureInitSpec {

    public final List<BlockSpec<?>> blocks;
    public final List<ItemSpec<?>> items;
    public final List<FluidSpec<?>> fluids;
    public final List<TileEntitySpec> tileEntities;
    public final List<EntitySpec> entities;
    public final List<GuiContainerSpec<?, ?>> containers;

    public FeatureInitSpec(List<BlockSpec<?>> blocks, List<ItemSpec<?>> items,
                           List<FluidSpec<?>> fluids, List<TileEntitySpec> tileEntities,
                           List<EntitySpec> entities, List<GuiContainerSpec<?, ?>> containers) {
        this.blocks = blocks;
        this.items = items;
        this.fluids = fluids;
        this.tileEntities = tileEntities;
        this.entities = entities;
        this.containers = containers;
    }

}
