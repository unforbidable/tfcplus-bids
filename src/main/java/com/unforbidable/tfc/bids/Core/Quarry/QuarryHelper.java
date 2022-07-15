package com.unforbidable.tfc.bids.Core.Quarry;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

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

    public static int getSideRequiringWedgesCount(World world, int x, int y, int z, IQuarriable quarriable, int side) {
        ForgeDirection orientation = ForgeDirection.getOrientation(side);
        int count = 0;

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            // Skip front and back relative to quarry location
            if (d != orientation && d != orientation.getOpposite()) {
                Block block = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
                if (quarriable.blockRequiresWedgesToDetach(block))
                    count++;
            }
        }

        return count;
    }

    public static boolean canQuarryBlockAt(World world, int x, int y, int z, Block block, int side) {
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        if (quarriable != null && quarriable.canQuarryBlockAt(world, x, y, z, side)) {
            return getSideRequiringWedgesCount(world, x, y, z, quarriable, side) > 0;
        }
        return false;
    }

}
