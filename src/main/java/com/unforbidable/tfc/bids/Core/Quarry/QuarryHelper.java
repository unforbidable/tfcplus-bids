package com.unforbidable.tfc.bids.Core.Quarry;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
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
                int metadata = world.getBlockMetadata(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
                if (quarriable.blockRequiresWedgesToDetach(block, metadata))
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

    public static boolean isQuarryReadyAt(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityQuarry) {
            int side = world.getBlockMetadata(x, y, z) & 7;
            ForgeDirection d = ForgeDirection.getOrientation(side);
            ForgeDirection o = d.getOpposite();
            int x2 = x + o.offsetX;
            int y2 = y + o.offsetY;
            int z2 = z + o.offsetZ;
            Block block = world.getBlock(x2, y2, z2);
            IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
            if (quarriable != null) {
                return quarriable.isQuarryReady(world, x2, y2, z2);
            }

            Bids.LOG.warn("Expected quarriable block at " + x2 + ", " + y2 + ", " + z2 + " side " + d);
        } else {
            Bids.LOG.warn("Expected quarry tile entity at " + x + ", " + y + ", " + z);
        }

        return false;
    }

}
