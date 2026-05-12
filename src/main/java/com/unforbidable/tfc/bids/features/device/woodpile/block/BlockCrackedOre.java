package com.unforbidable.tfc.bids.features.device.woodpile.block;

import com.dunk.tfc.Blocks.Terrain.BlockOre;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import net.minecraft.block.material.Material;

public class BlockCrackedOre extends BlockOre {

    public BlockCrackedOre() {
        super(Material.rock);

        setHardness(5F);
        setResistance(5F);
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(BlockNames.CRACKED_ORE);
    }

}
