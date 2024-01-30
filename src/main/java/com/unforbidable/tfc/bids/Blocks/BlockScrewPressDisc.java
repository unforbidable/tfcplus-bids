package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressBounds;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressDisc;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockScrewPressDisc extends BlockContainer {

    public BlockScrewPressDisc() {
        super(Material.wood);

        setHardness(2f);
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        int orientation = ScrewPressHelper.getOrientationFromMetadata(meta);
        AxisAlignedBB[] bounds = ScrewPressBounds.getBoundsForOrientation(orientation).getDisc();
        AxisAlignedBB bb = bounds[0]; // disc bottom
        setBlockBounds((float) bb.minX, 0f, (float) bb.minZ, (float) bb.maxX, 1f, (float) bb.maxZ);
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
    public int getRenderType() {
        return BidsBlocks.screwPressDiscRenderId;
    }

    @Override
    public IIcon getIcon(int i, int j) {
        return TFCBlocks.woodSupportH.getIcon(0, 0);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int orientation = ScrewPressHelper.getOrientationFromMetadata(world.getBlockMetadata(x, y, z));
        ForgeDirection[] ds = ScrewPressHelper.getNeighborDirectionsForOrientation(orientation);

        // Rack middle on both sides, rack bridge above and barrel below
        return world.getBlock(x + ds[0].offsetX, y, z + ds[0].offsetZ) == BidsBlocks.screwPressRackMiddle &&
            world.getBlock(x + ds[1].offsetX, y, z + ds[1].offsetZ) == BidsBlocks.screwPressRackMiddle &&
            world.getBlock(x, y + 1, z) == BidsBlocks.screwPressRackBridge &&
            world.getBlock(x, y - 1, z) == BidsBlocks.screwPressBarrel;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y + 1, z);
        int orientation = ScrewPressHelper.getOrientationFromMetadata(meta);
        ForgeDirection[] ds = ScrewPressHelper.getNeighborDirectionsForOrientation(orientation);
        return world.getBlock(x, y - 1, z) == BidsBlocks.screwPressBarrel &&
            world.getBlock(x, y + 1, z) == BidsBlocks.screwPressRackBridge &&
            world.getBlock(x + ds[0].offsetX, y, z + ds[0].offsetZ) == BidsBlocks.screwPressRackMiddle &&
            world.getBlock(x + ds[1].offsetX, y, z + ds[1].offsetZ) == BidsBlocks.screwPressRackMiddle;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is) {
        int meta = world.getBlockMetadata(x, y + 1, z);
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);

        TileEntityScrewPressBarrel teBarrel = (TileEntityScrewPressBarrel) world.getTileEntity(x, y - 1, z);
        teBarrel.onPressingDiscPlaced();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                ItemStack is = new ItemStack(Item.getItemFromBlock(this), 1, 0);
                TFC_Core.giveItemToPlayer(is, player);

                world.setBlockToAir(x, y, z);
            }
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityScrewPressDisc();
    }

}
