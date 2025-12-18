package com.unforbidable.tfc.bids.Core.SoakingSurface;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Blocks.BlockSoakingSurface;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySoakingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.SoakingSurfaceManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class SoakingSurfaceHelper {

    public static boolean canPlaceSoakingItemAt(World world, int x, int y, int z, ItemStack item) {
        if (isValidSoakingSurfaceItem(item, world, x, y, z)) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof BlockSoakingSurface) {
                TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
                return te.canPlaceItem(item);
            } else {
                return canSoakingSurfaceReplaceBlock(block);
            }
        }

        return false;
    }

    private static boolean isValidSoakingSurfaceItem(ItemStack item, World world, int x, int y, int z) {
        return SoakingSurfaceManager.findMatchingRecipe(item, world, x, y + 1, z) != null;
    }

    private static boolean isValidSoakingSurfaceBlock(Block block) {
        return TFC_Core.isSoil(block) || TFC_Core.isGravel(block) || TFC_Core.isSand(block);
    }

    private static boolean canSoakingSurfaceReplaceBlock(Block block) {
        return block.isOpaqueCube() && block.renderAsNormalBlock() && isValidSoakingSurfaceBlock(block);
    }

    public static boolean placeSoakingItemAt(World world, int x, int y, int z, ItemStack item) {
        if (isValidSoakingSurfaceItem(item, world, x, y, z)) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof BlockSoakingSurface) {
                TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
                if (te.canPlaceItem(item)) {
                    return te.placeItem(item);
                }
            } else {
                if (canSoakingSurfaceReplaceBlock(block)) {
                    int metadata = world.getBlockMetadata(x, y, z);
                    world.setBlock(x, y, z, BidsBlocks.soakingSurface, 0, 2);
                    TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
                    te.setOriginalBlock(block, metadata);
                    return te.placeItem(item);
                }
            }
        }

        return false;
    }

    private static int getSoakingSurfaceSlotFromHit(float hitX, float hitY, float hitZ) {
        final AxisAlignedBB near = AxisAlignedBB.getBoundingBox(hitX - 0.01f, hitY - 0.1f, hitZ - 0.01f,
            hitX + 0.01f, hitY + 0.1f, hitZ + 0.01f);

        for (int i = 0; i < TileEntitySoakingSurface.MAX_STORAGE; i++) {
            final AxisAlignedBB check = getSoakingSurfaceSlotBounds(i);

            if (check.intersectsWith(near)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean retrieveItemFromSoakingSurface(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int slot = getSoakingSurfaceSlotFromHit(hitX, hitY, hitZ);
        if (slot != -1) {
            TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
            return te.retrieveItem(slot, player);
        }

        return false;
    }

    public static Vec3 getSoakingSurfaceItemVector(int slot) {
        final int row = slot % 2;
        final int col = slot / 2;

        final float x = 0.25f + col * 0.5f;
        final float y = 1;
        final float z = 0.25f + row * 0.5f;

        return Vec3.createVectorHelper(x, y, z);
    }

    public static MovingObjectPosition onSoakingSurfaceCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
        startVec = startVec.addVector(-x, -y, -z);
        endVec = endVec.addVector(-x, -y, -z);

        CollisionInfo nearestCol = null;
        int nearestSlot = -1;

        for (int i = 0; i < TileEntitySoakingSurface.MAX_STORAGE; i++) {
            CollisionInfo col = CollisionHelper.rayTraceAABB(getSoakingSurfaceSlotBounds(i), startVec, endVec);

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

    private static AxisAlignedBB getSoakingSurfaceSlotBounds(int slot) {
        final int row = slot % 2;
        final int col = slot / 2;

        final float minX = col * 0.5f;
        final float minY = 0.9f;
        final float minZ = row * 0.5f;
        final float maxX = minX + 0.5f;
        final float maxY = 1.1f;
        final float maxZ = minZ + 0.5f;

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

}
