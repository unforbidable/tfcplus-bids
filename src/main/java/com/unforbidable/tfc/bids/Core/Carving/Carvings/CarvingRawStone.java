package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CarvingRawStone extends CarvingRoughStone {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == TFCBlocks.stoneSed;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return super.canCarveBlockAt(block, metadata, world, x, y, z, side)
                && isCarvedBlockExposed(world, x, y, z, side);
    }

    private boolean isCarvedBlockExposed(World world, int x, int y, int z, int side) {
        // The rules here are similar to carving bit rules
        // see TileEntityCarving.canCarveBit()
        // for consistency

        // Three sides must be exposed, or their opposing side
        if ((isBlockToSideExposed(world, x, y, z, ForgeDirection.WEST)
                || isBlockToSideExposed(world, x, y, z, ForgeDirection.EAST))
                && (isBlockToSideExposed(world, x, y, z, ForgeDirection.SOUTH)
                        || isBlockToSideExposed(world, x, y, z, ForgeDirection.NORTH))
                && (isBlockToSideExposed(world, x, y, z, ForgeDirection.UP)
                        || isBlockToSideExposed(world, x, y, z, ForgeDirection.DOWN))) {
            return true;
        }

        // A there must be a side that is entirely explosed (air)
        // and this exposed side must be surrounded by at least 4 exposed blocks
        // This allows diging into a wall, one block deep
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            final int xd = x + d.offsetX;
            final int yd = y + d.offsetY;
            final int zd = z + d.offsetZ;
            if (world.isAirBlock(xd, zd, yd)) {
                int exposedExposingSidesCount = 0;
                for (ForgeDirection ds : ForgeDirection.VALID_DIRECTIONS) {
                    // Skip back and front relative to the block becing carved out
                    if (ds != d && ds != d.getOpposite()) {
                        if (isBlockToSideExposed(world, xd, yd, zd, ds)) {
                            exposedExposingSidesCount++;
                        }
                    }
                }

                if (exposedExposingSidesCount == 4) {
                    return true;
                } else {
                    Bids.LOG.debug("Not enough exposed blocks around to " + d + ": " + exposedExposingSidesCount);
                }
            }
        }

        Bids.LOG.debug("Cannot carve raw block that is too deep");

        return false;
    }

    private boolean isBlockToSideExposed(World world, final int x, final int y, final int z, ForgeDirection d) {
        return !world.isSideSolid(x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite());
    }

}
