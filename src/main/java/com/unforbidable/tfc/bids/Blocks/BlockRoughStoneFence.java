package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Fences.FenceConnections;
import com.unforbidable.tfc.bids.Core.Fences.FenceHelper;
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
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockRoughStoneFence extends BlockPalisade {

    public final BlockRoughStone materialBlock;

    protected Block materialBlockTopBottom;

    public BlockRoughStoneFence(BlockRoughStone materialBlock) {
        super(materialBlock, 0);

        this.materialBlock = materialBlock;

        metaNames = null;

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setHardness(10f);
    }

    public BlockRoughStoneFence setMaterialBlockTopBottom(Block materialBlockTopBottom) {
        this.materialBlockTopBottom = materialBlockTopBottom;

        return this;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        List<ItemStack> materialBlockSubBlocks = new ArrayList<ItemStack>();
        materialBlock.getSubBlocks(item, tabs, materialBlockSubBlocks);

        for (ItemStack is : materialBlockSubBlocks) {
            list.add(new ItemStack(this, 1, is.getItemDamage()));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if ((side == 0 || side == 1) && materialBlockTopBottom != null) {
            return materialBlockTopBottom.getIcon(side, meta);
        }

        return materialBlock.getIcon(side, meta);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.roughStoneFenceRenderId;
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
            // Prevent falling blocks from breaking filled fence
            FenceConnections fc = new FenceConnections(world, x, y, z);
            return fc.canFenceFill();
        } else {
            return false;
        }
    }

}
