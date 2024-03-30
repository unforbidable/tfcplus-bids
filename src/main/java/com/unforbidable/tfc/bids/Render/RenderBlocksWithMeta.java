package com.unforbidable.tfc.bids.Render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlocksWithMeta extends RenderBlocks {

    public int overrideMetadata = 0;

    public RenderBlocksWithMeta(RenderBlocks renderBlocks) {
        super(renderBlocks.blockAccess);
    }

    @Override
    public IIcon getBlockIcon(Block p_147793_1_, IBlockAccess p_147793_2_, int p_147793_3_, int p_147793_4_, int p_147793_5_, int p_147793_6_) {
        return this.getIconSafe(p_147793_1_.getIcon(p_147793_6_, overrideMetadata));
    }

}
