package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Terrain.BlockOre3;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.material.Material;

public class BlockCrackedOre3 extends BlockOre3 {

    public BlockCrackedOre3(Material material) {
        super(material);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.crackedOreRenderId;
    }

}
