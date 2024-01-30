package com.unforbidable.tfc.bids.Core.ScrewPress;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressLever;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.ScrewPressManager;
import com.unforbidable.tfc.bids.api.Crafting.ScrewPressRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ScrewPressHelper {

    private static final ForgeDirection[][] DIRECTIONS_FOR_ORIENTATION = new ForgeDirection[][] {
        new ForgeDirection[] { ForgeDirection.WEST, ForgeDirection.EAST },
        new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH }
    };

    public static int getOrientationFromMetadata(int blockMetadata) {
        return blockMetadata & 1;
    }

    public static int getMetadataForOrientation(int orientation) {
        return orientation;
    }

    public static ForgeDirection[] getNeighborDirectionsForOrientation(int orientation) {
        return DIRECTIONS_FOR_ORIENTATION[orientation];
    }

    public static ForgeDirection getValidDirectionToPlaceTopRackAt(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y - 1, z);
        int orientation = ScrewPressHelper.getOrientationFromMetadata(meta);
        ForgeDirection[] dirs = ScrewPressHelper.getNeighborDirectionsForOrientation(orientation);
        for (ForgeDirection d : dirs) {
            if (world.isAirBlock(x + d.offsetX, y, z + d.offsetZ) &&
                world.isAirBlock(x + d.offsetX * 2, y, z + d.offsetZ * 2) &&
                world.getBlock(x + d.offsetX * 2, y - 1, z + d.offsetZ * 2) == BidsBlocks.screwPressRackMiddle) {
                return d;
            }
        }

        return ForgeDirection.UNKNOWN;
    }

    public static ForgeDirection getValidDirectionToPlaceLever(World world, int x, int y, int z) {
        for (ForgeDirection d : new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST }) {
            if (world.getBlock(x - d.offsetX, y, z - d.offsetZ) == BidsBlocks.screwPressRackMiddle &&
                world.getBlock(x - d.offsetX * 2, y + 1, z - d.offsetZ * 2) == BidsBlocks.screwPressRackBridge &&
                world.getBlock(x - d.offsetX * 3, y, z - d.offsetZ * 3) == BidsBlocks.screwPressRackMiddle) {

                TileEntity te = world.getTileEntity(x - d.offsetX * 4, y, z - d.offsetZ * 4);
                if (te instanceof TileEntityScrewPressLever) {
                    Bids.LOG.debug("Already lever placed");
                } else {
                    return d;
                }
            }
        }

        return ForgeDirection.UNKNOWN;
    }

    public static AxisAlignedBB getLeverBlockBoundsAt(IBlockAccess world, int x, int y, int z) {
        TileEntityScrewPressLever teLever = (TileEntityScrewPressLever) world.getTileEntity(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        int orientation = getOrientationFromMetadata(meta);
        ForgeDirection rackDirection = teLever.getScrewDirection().getOpposite();

        AxisAlignedBB bb = ScrewPressBounds.getBoundsForOrientation(orientation).getLeverAll()
            .getOffsetBoundingBox(rackDirection.offsetX, 0, rackDirection.offsetZ);

        bb.minY = teLever.getScrewPressDiscTileEntity() != null ? 0 : 0.8;
        bb.maxY = 2;

        return bb;
    }

    public static boolean isValidScrewPressInputItem(ItemStack itemStack) {
        return ScrewPressManager.hasMatchingRecipe(itemStack);
    }

    public static float getScrewPressInputItemHardness(ItemStack itemStack) {
        ScrewPressRecipe recipe = ScrewPressManager.getMatchingRecipe(itemStack);
        if (recipe != null) {
            return recipe.getResistance();
        } else {
            // Somehow an item that cannot be pressed ended up in the basket
            return Float.MAX_VALUE;
        }
    }

}
