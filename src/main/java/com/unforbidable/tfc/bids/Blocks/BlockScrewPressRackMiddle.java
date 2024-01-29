package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressBounds;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressDisc;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockScrewPressRackMiddle extends BlockScrewPressRackPart {

    public BlockScrewPressRackMiddle() {
    }

    @Override
    public AxisAlignedBB[] getIndividualBoundsForOrientation(int orientation) {
        return ScrewPressBounds.getBoundsForOrientation(orientation).getRackMiddle();
    }

    @Override
    public AxisAlignedBB[] getInventoryBlockBounds() {
        return ScrewPressBounds.getBounds().getRackBottomInv();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int orientation = ScrewPressHelper.getOrientationFromMetadata(world.getBlockMetadata(x, y, z));
        AxisAlignedBB bb = ScrewPressBounds.getBoundsForOrientation(orientation).getRackMiddleAll();
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Bids.LOG.info("meta (middle): " + world.getBlockMetadata(x, y, z));
        return world.getBlock(x, y - 1, z) == BidsBlocks.screwPressRackBottom;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

}
