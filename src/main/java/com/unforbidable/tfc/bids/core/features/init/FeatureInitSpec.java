package com.unforbidable.tfc.bids.core.features.init;

import com.unforbidable.tfc.bids.core.features.init.block.BlockSpec;
import com.unforbidable.tfc.bids.core.features.init.gui.GuiContainerSpec;
import com.unforbidable.tfc.bids.core.features.init.item.ItemSpec;
import com.unforbidable.tfc.bids.core.features.init.tileentity.TileEntitySpec;

import java.util.List;

public class FeatureInitSpec {

    public final List<BlockSpec<?>> blocks;
    public final List<ItemSpec<?>> items;
    public final List<TileEntitySpec> tileEntities;
    public final List<GuiContainerSpec<?, ?>> containers;

    public FeatureInitSpec(List<BlockSpec<?>> blocks, List<ItemSpec<?>> items,
                           List<TileEntitySpec> tileEntities,
                           List<GuiContainerSpec<?, ?>> containers) {
        this.blocks = blocks;
        this.items = items;
        this.tileEntities = tileEntities;
        this.containers = containers;
    }

}
