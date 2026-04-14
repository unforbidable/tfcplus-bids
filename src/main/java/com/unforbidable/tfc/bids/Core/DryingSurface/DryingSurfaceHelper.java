package com.unforbidable.tfc.bids.Core.DryingSurface;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemClothing;
import com.unforbidable.tfc.bids.Blocks.BlockDryingSurface;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.DryingSurfaceManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DryingSurfaceHelper {

    public static boolean canPlaceDryingItemAt(World world, int x, int y, int z, ItemStack item) {
        if (isValidDryingSurfaceItem(item)) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof BlockDryingSurface) {
                TileEntityDryingSurface te = (TileEntityDryingSurface) world.getTileEntity(x, y, z);
                return te.canPlaceItem(item);
            } else {
                return world.isAirBlock(x, y + 1, z) && block.isSideSolid(world, x, y, z, ForgeDirection.UP);
            }
        }

        return false;
    }

    private static boolean isValidDryingSurfaceItem(ItemStack item) {
        return DryingSurfaceManager.getMatchingRecipe(item) != null || item.getItem() instanceof ItemClothing;
    }

    public static boolean placeDryingItemAt(World world, int x, int y, int z, ItemStack item) {
        if (isValidDryingSurfaceItem(item)) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof BlockDryingSurface) {
                TileEntityDryingSurface te = (TileEntityDryingSurface) world.getTileEntity(x, y, z);
                if (te.canPlaceItem(item)) {
                    return te.placeItem(item);
                }
            } else {
                world.setBlock(x, y + 1, z, BidsBlocks.dryingSurface, 0, 2);
                TileEntityDryingSurface te = (TileEntityDryingSurface) world.getTileEntity(x, y + 1, z);
                return te.placeItem(item);
            }
        }

        return false;
    }

    private static int getDryingSurfaceSlotFromHit(float hitX, float hitY, float hitZ) {
        final AxisAlignedBB near = AxisAlignedBB.getBoundingBox(hitX - 0.01f, hitY - 0.1f, hitZ - 0.01f,
            hitX + 0.01f, hitY + 0.1f, hitZ + 0.01f);

        for (int i = 0; i < TileEntityDryingSurface.MAX_STORAGE; i++) {
            final AxisAlignedBB check = getDryingSurfaceSlotBounds(i);

            if (check.intersectsWith(near)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean retrieveItemFromDryingSurface(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int slot = getDryingSurfaceSlotFromHit(hitX, hitY, hitZ);
        if (slot != -1) {
            TileEntityDryingSurface te = (TileEntityDryingSurface) world.getTileEntity(x, y, z);
            return te.retrieveItem(slot, player);
        }

        return false;
    }

    public static Vec3 getDryingSurfaceItemVector(int slot) {
        final int row = slot % 2;
        final int col = slot / 2;

        final float x = 0.25f + col * 0.5f;
        final float y = 1;
        final float z = 0.25f + row * 0.5f;

        return Vec3.createVectorHelper(x, y, z);
    }

    public static MovingObjectPosition onDryingSurfaceCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        TileEntityDryingSurface te = (TileEntityDryingSurface) world.getTileEntity(x, y, z);
        startVec = startVec.addVector(-x, -y, -z);
        endVec = endVec.addVector(-x, -y, -z);

        CollisionInfo nearestCol = null;
        int nearestSlot = -1;

        for (int i = 0; i < TileEntityDryingSurface.MAX_STORAGE; i++) {
            CollisionInfo col = CollisionHelper.rayTraceAABB(getDryingSurfaceSlotBounds(i), startVec, endVec);

            // When the item collides
            // Save if first or closer than the nearest so far
            if (col != null && (nearestCol == null || col.distance < nearestCol.distance)) {
                nearestCol = col;
                nearestSlot = i;
            }
        }

        if (nearestSlot != -1) {
            te.setSelectedSlot(nearestSlot);
        } else {
            te.clearSelectedSection();
        }

        return null;
    }

    private static AxisAlignedBB getDryingSurfaceSlotBounds(int slot) {
        final int row = slot % 2;
        final int col = slot / 2;

        final float minX = col * 0.5f;
        final float minY = 0f;
        final float minZ = row * 0.5f;
        final float maxX = minX + 0.5f;
        final float maxY = 0.1f;
        final float maxZ = minZ + 0.5f;

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

}
