package com.unforbidable.tfc.bids.Core.DryingRack;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingRack;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DryingRackHelper {

    private static int getRequiredDryingRackSpan(World world, int x, int y, int z, ForgeDirection side, int max) {
        int span = 0;
        int x2 = x + side.offsetX;
        int z2 = z + side.offsetZ;

        while (span < max && world.isAirBlock(x2, y, z2)) {
            span++;
            x2 += side.offsetX;
            z2 += side.offsetZ;
        }

        if (span == 0) {
            Bids.LOG.debug("Not enough room to place drying rack here");
            return 0;
        }

        if (canPlaceDryingRackAt(world, x2, y, z2, side.getOpposite())) {
            Bids.LOG.debug("Drying rack can be placed spanning over " + span + " block(s)");
            return span;
        }

        Bids.LOG.debug("No sufficiently solid block on the opposite side to place draying rack here");

        return 0;
    }

    public static boolean canPlaceDryingRackAt(World world, int x, int y, int z, ForgeDirection side) {
        return side != ForgeDirection.UP && side != ForgeDirection.DOWN
                && world.isSideSolid(x, y, z, side)
                && world.isAirBlock(x + side.offsetX, y, z + side.offsetZ);
    }

    public static void placeDryingRackAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
            ForgeDirection side, int maxSpan) {
        final int veryMaxSpan = Math.min(maxSpan, stack.stackSize);
        final int span = DryingRackHelper.getRequiredDryingRackSpan(world, x, y, z, side, veryMaxSpan);
        final int orientation = side.ordinal() / 2 - 1;

        int x2 = x;
        int z2 = z;

        for (int i = 0; i < span; i++) {
            x2 += side.offsetX;
            z2 += side.offsetZ;

            if (world.isAirBlock(x2, y, z2)) {
                world.setBlock(x2, y, z2, BidsBlocks.dryingRack, orientation % 4, 2);
                TileEntityDryingRack te = (TileEntityDryingRack) world.getTileEntity(x2, y, z2);
                te.setOrientation(orientation);
                stack.stackSize--;
            }
        }
    }

    public static MovingObjectPosition onDryingRackCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec,
            Vec3 endVec) {
        TileEntityDryingRack te = (TileEntityDryingRack) world.getTileEntity(x, y, z);
        startVec = startVec.addVector(-x, -y, -z);
        endVec = endVec.addVector(-x, -y, -z);

        CollisionInfo nearestCol = null;
        int nearestSection = -1;

        DryingRackBounds dryingRackBounds = DryingRackBounds.fromOrientation(te.getOrientation());
        AxisAlignedBB[] bounds = dryingRackBounds.sections;
        for (int i = 0; i < bounds.length; i++) {
            CollisionInfo col = CollisionHelper.rayTraceAABB(bounds[i], startVec, endVec);

            // When the item collides
            // Save if first or closer than the nearest so far
            if (col != null && (nearestCol == null || col.distance < nearestCol.distance)) {
                nearestCol = col;
                nearestSection = i;
            }
        }

        if (nearestSection != -1) {
            te.setSelectedSection(nearestSection);
        } else {
            te.clearSelectedSection();
        }

        return null;
    }

    public static boolean placeItemOnDryingRackAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y,
            int z, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && isItemValidDryingRackItem(itemStack)) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityDryingRack) {
                TileEntityDryingRack dryingRack = (TileEntityDryingRack) te;
                int section = getDryingRackSectionFromHit(dryingRack, hitX, hitY, hitZ);
                if (section != -1 && dryingRack.placeItem(section, itemStack)) {
                    itemStack.stackSize--;
                }
            }
        }

        return false;
    }

    public static boolean retrieveItemFromDryingRackAt(EntityPlayer player, World world, int x, int y, int z,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityDryingRack) {
                TileEntityDryingRack dryingRack = (TileEntityDryingRack) te;
                int section = getDryingRackSectionFromHit(dryingRack, hitX, hitY, hitZ);
                if (section != -1 && dryingRack.retrieveItem(section, player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isItemValidDryingRackItem(ItemStack itemStack) {
        return DryingManager.hasMatchingRecipe(itemStack);
    }

    public static int getDryingRackSectionFromHit(TileEntityDryingRack dryingRack, float hitX, float hitY, float hitZ) {
        DryingRackBounds bounds = DryingRackBounds.fromOrientation(dryingRack.getOrientation());
        AxisAlignedBB near = AxisAlignedBB.getBoundingBox(hitX - 0.001f, hitY - 0.001f, hitZ - 0.001f,
                hitX + 0.001f, hitY + 0.001f, hitZ + 0.001f);

        for (int i = 0; i < bounds.sections.length; i++) {
            AxisAlignedBB check = bounds.sections[i];

            if (check.intersectsWith(near)) {
                return i;
            }
        }

        return -1;
    }

}
