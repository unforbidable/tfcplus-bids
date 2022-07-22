package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Terrain.BlockCollapsible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRoughStoneBrick extends BlockRoughStone {

    public BlockRoughStoneBrick() {
        super();
    }

    @Override
    protected boolean canBlockFall(World world, int x, int y, int z) {
        // A rough stone brick block will not fall
        // if it is touching at least two other rough stone bricks blocks
        // on two opposing sides
        // both those neighbor bricks must be supported from bellow
        if (isNeighborSupportedBrick(world, x, y, z, ForgeDirection.EAST)
                && isNeighborSupportedBrick(world, x, y, z, ForgeDirection.WEST)
                || isNeighborSupportedBrick(world, x, y, z, ForgeDirection.SOUTH)
                        && isNeighborSupportedBrick(world, x, y, z, ForgeDirection.NORTH)) {
            return false;
        }

        return super.canBlockFall(world, x, y, z);
    }

    private boolean isNeighborSupportedBrick(World world, int x, int y, int z, ForgeDirection d) {
        Block neighbor = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        return neighbor.isSideSolid(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite())
                && (!BlockCollapsible.canFallBelow(world, x + d.offsetX, y + d.offsetY - 1, z + d.offsetZ)
                        || world.getTileEntity(x + d.offsetX, y + d.offsetY - 1,
                                z + d.offsetZ) instanceof TileEntityCarving);
    }

}
