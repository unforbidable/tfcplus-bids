package com.unforbidable.tfc.bids.Blocks;

import net.minecraft.world.World;

public class BlockRoughStoneBrick extends BlockRoughStone {

    public BlockRoughStoneBrick() {
        super();
    }

    @Override
    protected boolean canBlockFall(World world, int x, int y, int z) {
        // A rough stone brick block will never fall
        return false;
    }

}
