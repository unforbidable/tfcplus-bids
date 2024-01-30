package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressBounds;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockScrewPressRackBridge extends BlockScrewPressRackPart {

    public BlockScrewPressRackBridge() {
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public AxisAlignedBB[] getIndividualBoundsForOrientation(int orientation) {
        return ScrewPressBounds.getBoundsForOrientation(orientation).getRackBridge();
    }

    @Override
    public AxisAlignedBB[] getInventoryBlockBounds() {
        return ScrewPressBounds.getBounds().getRackTopInv();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int orientation = ScrewPressHelper.getOrientationFromMetadata(world.getBlockMetadata(x, y, z));
        AxisAlignedBB bb = ScrewPressBounds.getBoundsForOrientation(orientation).getRackBridgeAll();
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int orientation = ScrewPressHelper.getOrientationFromMetadata(world.getBlockMetadata(x, y, z));
        ForgeDirection[] ds = ScrewPressHelper.getNeighborDirectionsForOrientation(orientation);

        // Rack top on both sides
        return world.getBlock(x + ds[0].offsetX, y, z + ds[0].offsetZ) == BidsBlocks.screwPressRackTop &&
            world.getBlock(x + ds[1].offsetX, y, z + ds[1].offsetZ) == BidsBlocks.screwPressRackTop;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z) == BidsBlocks.screwPressRackMiddle &&
            ScrewPressHelper.getValidDirectionToPlaceTopRackAt(world, x, y, z) != ForgeDirection.UNKNOWN;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is) {
        int meta = world.getBlockMetadata(x, y - 1, z);
        ForgeDirection d = ScrewPressHelper.getValidDirectionToPlaceTopRackAt(world, x, y, z);

        world.setBlock(x, y, z, BidsBlocks.screwPressRackTop, meta, 2);
        world.setBlock(x + d.offsetX, y, z + d.offsetZ, BidsBlocks.screwPressRackBridge, meta, 2);
        world.setBlock(x + d.offsetX * 2, y, z + d.offsetZ * 2, BidsBlocks.screwPressRackTop, meta, 2);
    }

}
