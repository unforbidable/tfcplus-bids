package com.unforbidable.tfc.bids.features.device.dryingrack.block;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackAnchorBlock;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackHelper;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackSideBounds;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDryingRackSide extends Block implements DryingRackAnchorBlock {

    public BlockDryingRackSide() {
        super(Material.wood);

        setHardness(2);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        float hardness = super.getBlockHardness(world, x, y, z);

        // Increased hardness when drying rack side above
        // or drying rack connected to any side
        Block above = world.getBlock(x, y + 1, z);
        if (above instanceof BlockDryingRackSide) {
            return hardness * 4;
        } else {
            int orientation = world.getBlockMetadata(x, y, z) % 2;
            ForgeDirection d = ForgeDirection.getOrientation(orientation * 2 + 2);
            ForgeDirection o = d.getOpposite();
            if (world.getBlock(x + d.offsetX, y, z + d.offsetZ) instanceof BlockDryingRack ||
                world.getBlock(x + o.offsetX, y, z + o.offsetZ) instanceof BlockDryingRack) {
                return hardness * 4;
            }
        }

        return hardness;
    }

    @Override
    public int damageDropped(int dmg) {
        return 0;
    }

    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune) {
        return TFCItems.pole;
    }

    @Override
    public int quantityDropped(Random rand) {
        return 2;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        AxisAlignedBB bounds = DryingRackSideBounds.getEntireDryingRackSideBoundsForOrientation(meta & 1);
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ,
            (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return DryingRackSideBounds.getEntireDryingRackSideBoundsForOrientation(meta & 1)
            .offset(x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y ,z)) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
            world.notifyBlockChange(x, y, z, Blocks.air);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Block below = world.getBlock(x, y - 1, z);
        return below instanceof BlockDryingRackSide || below.isSideSolid(world, x, y - 1, z, ForgeDirection.UP);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
                                    float hitY, float hitZ) {
        if (player.getHeldItem() != null) {
            ItemStack heldItem = player.getHeldItem();
            if (heldItem.getItem() == Item.getItemFromBlock(TFCBlocks.thatch) &&
                DryingRackHelper.canPlaceDryingRackCoverAbove(world, x, y, z)) {
                DryingRackHelper.placeDryingRackCoverAbove(heldItem, player, world, x, y, z);
            }
        }

        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return BidsBlocks.dryingRack.getIcon(side, meta);
    }

    @Override
    public boolean canDryingRackAttach(World world, int x, int y, int z, ForgeDirection side) {
        int orientation = world.getBlockMetadata(x, y, z) % 2;
        return orientation == 0 && (side == ForgeDirection.NORTH || side == ForgeDirection.SOUTH) ||
            orientation == 1 && (side == ForgeDirection.EAST || side == ForgeDirection.WEST);
    }

}
