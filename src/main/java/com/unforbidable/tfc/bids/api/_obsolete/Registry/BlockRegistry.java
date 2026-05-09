package com.unforbidable.tfc.bids.api._obsolete.Registry;

import net.minecraft.block.Block;

public class BlockRegistry<T> extends MapRegistry<Block, T> {

    @Override
    protected String getName(Block key) {
        return key.getUnlocalizedName();
    }

}
