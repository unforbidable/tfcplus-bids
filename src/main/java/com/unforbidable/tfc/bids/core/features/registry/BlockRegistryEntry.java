package com.unforbidable.tfc.bids.core.features.registry;

import com.unforbidable.tfc.bids.core.features.init.block.BlockSpec;
import net.minecraft.block.Block;

public class BlockRegistryEntry {

    public final BlockSpec<?> spec;
    public final Block instance;
    public final int id;

    public BlockRegistryEntry(BlockSpec<?> spec, Block instance, int id) {
        this.spec = spec;
        this.instance = instance;
        this.id = id;
    }

}
