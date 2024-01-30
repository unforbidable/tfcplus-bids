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
import net.minecraft.world.World;

public class BlockScrewPressRackBottom extends BlockScrewPressRackPart {

    public BlockScrewPressRackBottom() {
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public AxisAlignedBB[] getIndividualBoundsForOrientation(int orientation) {
        return ScrewPressBounds.getBoundsForOrientation(orientation).getRackBottom();
    }

    @Override
    public AxisAlignedBB[] getInventoryBlockBounds() {
        return ScrewPressBounds.getBounds().getRackBottomInv();
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlock(x, y + 1, z) == BidsBlocks.screwPressRackMiddle;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isAirBlock(x, y + 1, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is) {
        int orientation = (int) Math.floor(player.rotationYaw * 4F / 360F + 0.5D) & 1;
        int meta = ScrewPressHelper.getMetadataForOrientation(orientation);

        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
        world.setBlock(x, y + 1, z, BidsBlocks.screwPressRackMiddle, meta, 2);
    }

}
