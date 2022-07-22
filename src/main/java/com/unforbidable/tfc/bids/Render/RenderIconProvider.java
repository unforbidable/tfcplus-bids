package com.unforbidable.tfc.bids.Render;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class RenderIconProvider {

    Block block;
    int meta;

    public RenderIconProvider(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public IIcon getIcon(int side) {
        return block.getIcon(side, meta);
    }

}
