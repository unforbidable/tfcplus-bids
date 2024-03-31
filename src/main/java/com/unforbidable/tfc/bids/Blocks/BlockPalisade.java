package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Vanilla.BlockCustomWall;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Fences.FenceConnections;
import com.unforbidable.tfc.bids.Core.Fences.FenceHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockPalisade extends BlockCustomWall {

    private final int offset;
    protected String[] metaNames;

    public BlockPalisade(Block block, int offset) {
        super(block, 0);

        this.metaNames = WoodHelper.getWoodOffsetNames(offset);
        this.offset = offset;

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setHardness(4f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < metaNames.length; i++) {
            if (WoodHelper.canBuildLogWall(offset + i)) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (offset == 0) {
            return BidsBlocks.logWallVert.getIcon(side, meta);
        } else if (offset == 16) {
            return BidsBlocks.logWallVert2.getIcon(side, meta);
        } else {
            return BidsBlocks.logWallVert3.getIcon(side, meta);
        }
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.palisadeRenderId;
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
    public int damageDropped(int damage) {
        return damage;
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
        return block.renderAsNormalBlock() && block.getMaterial().isOpaque();
    }

    public boolean canFenceFillWithBlock(Block block) {
        return block.renderAsNormalBlock() && block.getMaterial().isOpaque();
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        FenceConnections fc = new FenceConnections(world, x, y, z);

        AxisAlignedBB bounds;
        if (entity instanceof EntityPlayer) {
            float width = 1f / 3;
            float height = world.isAirBlock(x, y + 1, z) && fc.countConnections() == 1 ? 0.5f : (fc.canFenceFill() ? 1f : 1.5f);
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

}
