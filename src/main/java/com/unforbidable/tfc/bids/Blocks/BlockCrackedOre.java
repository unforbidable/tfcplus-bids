package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Terrain.BlockOre;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.material.Material;

public class BlockCrackedOre extends BlockOre {

    public BlockCrackedOre(Material material) {
        super(material);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.crackedOreRenderId;
    }

}
