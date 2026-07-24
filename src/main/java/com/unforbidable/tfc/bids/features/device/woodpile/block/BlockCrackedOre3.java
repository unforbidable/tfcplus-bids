package com.unforbidable.tfc.bids.features.device.woodpile.block;

import com.dunk.tfc.Blocks.Terrain.BlockOre3;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import net.minecraft.block.material.Material;

public class BlockCrackedOre3 extends BlockOre3 {

    public BlockCrackedOre3() {
        super(Material.rock);

        setHardness(5F);
        setResistance(5F);
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

}
