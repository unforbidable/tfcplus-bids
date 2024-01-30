package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressBounds;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class BlockScrewPressRackTop extends BlockScrewPressRackPart {

    public BlockScrewPressRackTop() {
    }

    @Override
    public AxisAlignedBB[] getIndividualBoundsForOrientation(int orientation) {
        return ScrewPressBounds.getBoundsForOrientation(orientation).getRackTop();
    }

    @Override
    public AxisAlignedBB[] getInventoryBlockBounds() {
        return ScrewPressBounds.getBounds().getRackTopInv();
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int orientation = ScrewPressHelper.getOrientationFromMetadata(world.getBlockMetadata(x, y, z));
        ForgeDirection[] ds = ScrewPressHelper.getNeighborDirectionsForOrientation(orientation);

        // Rack middle below and rack bridge on either side
        return world.getBlock(x, y - 1, z) == BidsBlocks.screwPressRackMiddle &&
            (world.getBlock(x + ds[0].offsetX, y, z + ds[0].offsetZ) == BidsBlocks.screwPressRackBridge ||
                world.getBlock(x + ds[1].offsetX, y, z + ds[1].offsetZ) == BidsBlocks.screwPressRackBridge);
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
