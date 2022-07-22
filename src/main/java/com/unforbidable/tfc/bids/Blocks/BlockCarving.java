package com.unforbidable.tfc.bids.Blocks;

import java.util.List;
import java.util.Random;

import com.unforbidable.tfc.bids.Core.Carving.CarvingHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCarving extends BlockContainer {

    public BlockCarving(Material material) {
        super(material);
        setHardness(10);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityCarving();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        return CarvingHelper.onCarvingCollisionRayTrace(world, x, y, z, startVec, endVec);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess bAccess, int i, int j, int k) {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        Block block = world.getBlock(x, y, z);
        if (!(block instanceof BlockCarving)) {
            // Bids.LOG.warn("For some reason, BlockCarving is being accessed about
            // solidness of side " +
            // side + " at " + x + ", " + y + ", " + z + " but the block is not
            // BlockCarving, the block is " +
            // block.getUnlocalizedName() + " and we will refer to this block for the
            // resolution instead.");
            // Bids.LOG.warn("Here is the stack dump for this incident, to assist you in
            // identifying the cause.");
            // Thread.dumpStack();
            return block.isSideSolid(world, x, y, z, side);
        }

        TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);

        int startD = TileEntityCarving.CARVING_DIMENSION - 1;
        int endD = TileEntityCarving.CARVING_DIMENSION;

        int startX = (side == ForgeDirection.EAST ? startD : 0);
        int endX = (side == ForgeDirection.WEST ? 1 : endD);

        int startY = (side == ForgeDirection.UP ? startD : 0);
        int endY = (side == ForgeDirection.DOWN ? 1 : endD);

        int startZ = (side == ForgeDirection.SOUTH ? startD : 0);
        int endZ = (side == ForgeDirection.NORTH ? 1 : endD);

        // A single block removed from a side means the side is not solid
        int transpCount = 0;

        for (int subX = startX; subX < endX; ++subX)
            for (int subY = startY; subY < endY; ++subY)
                for (int subZ = startZ; subZ < endZ; ++subZ)
                    if (te.isBitCarved(subX, subY, subZ) && --transpCount < 0)
                        return false;

        return true;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.carvingRenderId;
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess bAccess, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);
        te.onCarvingBroken();

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata,
            EntityPlayer player) {
        CarvingHelper.onCarvingHarvested(world, x, y, z, player);

        super.onBlockHarvested(world, x, y, z, metadata, player);
    }

    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list,
            Entity entity) {
        TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);
        int dimension = TileEntityCarving.CARVING_DIMENSION;
        float div = 1f / dimension;

        for (int subX = 0; subX < dimension; subX++) {
            for (int subY = 0; subY < dimension; subY++) {
                for (int subZ = 0; subZ < dimension; subZ++) {
                    if (!te.isBitCarved(subX, subY, subZ)) {
                        float minX = subX * div;
                        float maxX = minX + div;
                        float minY = subY * div;
                        float maxY = minY + div;
                        float minZ = subZ * div;
                        float maxZ = minZ + div;

                        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
                        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
                    }
                }
            }
        }

        // This seems to do little to reduce the flickering of the sub click selection
        // so its disabled for now
        // CarvingHelper.updateBlockBoundsAfterCollisions(world, x, y, z);
    }

}
