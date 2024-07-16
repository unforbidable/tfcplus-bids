package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Terrain.BlockOre2;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockCrackedOre2 extends BlockOre2 {

    public BlockCrackedOre2(Material material) {
        super(material);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.crackedOreRenderId;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        return super.removedByPlayer(world, player, x, y, z);
    }

}
