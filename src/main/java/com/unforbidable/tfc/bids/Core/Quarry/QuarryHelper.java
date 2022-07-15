package com.unforbidable.tfc.bids.Core.Quarry;

import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class QuarryHelper {

    public static boolean isBlockQuarry(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == BidsBlocks.quarry;
    }

    public static boolean isBlockQuarry(World world, int x, int y, int z, ForgeDirection d) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        return block == BidsBlocks.quarry && meta == d.ordinal();
    }

}
