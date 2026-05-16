package com.unforbidable.tfc.bids.common.block;

import com.dunk.tfc.Blocks.Vanilla.BlockCustomWall;
import com.unforbidable.tfc.bids.util.fence.FenceConnections;
import com.unforbidable.tfc.bids.util.fence.FenceHelper;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCommonFence extends BlockCustomWall {

    protected String[] metaNames;

    public BlockCommonFence(Block blk, int t) {
        super(blk, t);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess bAccess, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    public boolean canFenceConnectToFence(Block block) {
        return FenceHelper.isFenceBlock(block) || FenceHelper.isFenceGateBlock(block);
    }

    public boolean canFenceConnectToBlock(Block block) {
        return block.renderAsNormalBlock() && block.isOpaqueCube() && block.getMaterial().isOpaque();
    }

    public boolean canFenceFillWithBlock(Block block) {
        return block.renderAsNormalBlock() && block.isOpaqueCube() && block.getMaterial().isOpaque();
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        FenceConnections fc = new FenceConnections(world, x, y, z);

        AxisAlignedBB bounds;
        if (entity instanceof EntityPlayer) {
            float width = 1f / 3;
            float height = world.isAirBlock(x, y + 1, z) && fc.getConnectionCount() == 1 ? 0.5f : (fc.canFenceFill() ? 1f : 1.5f);
            bounds = fc.getAllBounds(width, height)
                .offset(x, y, z);
        } else {
            bounds = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + (fc.canFenceFill() ? 1f : 1.5f), z + 1);
        }

        if (aabb.intersectsWith(bounds)) {
            list.add(bounds);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        FenceConnections fc = new FenceConnections(world, x, y, z);
        float width = 1f / 3;
        AxisAlignedBB bounds = fc.getAllBounds(width, 1f);
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        if (side == ForgeDirection.UP) {
            // Prevent falling blocks from breaking filled palisades
            FenceConnections fc = new FenceConnections(world, x, y, z);
            return fc.canFenceFill();
        } else {
            return false;
        }
    }
}
