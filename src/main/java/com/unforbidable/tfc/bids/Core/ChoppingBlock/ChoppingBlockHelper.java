package com.unforbidable.tfc.bids.Core.ChoppingBlock;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChoppingBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ChoppingBlockHelper {

    static final Vec3[] itemVectors = new Vec3[TileEntityChoppingBlock.MAX_STORAGE];
    static final AxisAlignedBB[] slotBounds = new AxisAlignedBB[TileEntityChoppingBlock.MAX_STORAGE];
    static final Vec3 centerVector = Vec3.createVectorHelper(0.5, getChoppingBlockBoundsHeight(), 0.5);

    public static float getChoppingBlockBoundsHeight() {
        return 0.8f;
    }

    public static Vec3 getChoppingBlockItemVector(int slot) {
        if (itemVectors[slot] == null) {
            final int row = slot % 2;
            final int col = slot / 2;

            final float x = 0.25f + col * 0.5f;
            final float y = getChoppingBlockBoundsHeight();
            final float z = 0.25f + row * 0.5f;

            itemVectors[slot] = Vec3.createVectorHelper(x, y, z);
        }

        return itemVectors[slot];
    }

    public static Vec3 getChoppingBlockCenterVector() {
        return centerVector;
    }

    public static AxisAlignedBB getChoppingBlockSlotBounds(int slot) {
        if (slotBounds[slot] == null) {
            final int row = slot % 2;
            final int col = slot / 2;

            final float minX = col * 0.5f;
            final float minY = getChoppingBlockBoundsHeight() - 0.1f;
            final float minZ = row * 0.5f;
            final float maxX = minX + 0.5f;
            final float maxY = getChoppingBlockBoundsHeight();
            final float maxZ = minZ + 0.5f;

            slotBounds[slot] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return slotBounds[slot];
    }

    public static MovingObjectPosition onChoppingBlockCollisionRayTrace(World world, int x, int y, int z,
            Vec3 startVec, Vec3 endVec) {
        TileEntityChoppingBlock te = (TileEntityChoppingBlock) world.getTileEntity(x, y, z);
        startVec = startVec.addVector(-x, -y, -z);
        endVec = endVec.addVector(-x, -y, -z);

        CollisionInfo nearestCol = null;
        int nearestSlot = -1;

        for (int i = 0; i < TileEntityChoppingBlock.MAX_STORAGE; i++) {
            CollisionInfo col = CollisionHelper.rayTraceAABB(getChoppingBlockSlotBounds(i), startVec, endVec);

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

    public static boolean placeItemOnChoppingBlockAt(ItemStack itemStack, EntityPlayer player, World world,
            int x, int y, int z, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            final TileEntityChoppingBlock choppingBlock = (TileEntityChoppingBlock) world.getTileEntity(x, y, z);
            final int slot = getChoppingBlockSlotFromHit(choppingBlock, hitX, hitY, hitZ);

            if (slot != -1) {
                final boolean isInput = choppingBlock.isChoppingBlockInput(itemStack);
                final boolean isTool = choppingBlock.isChoppingBlockTool(itemStack);

                if (isTool) {
                    if (choppingBlock.useTool(slot, itemStack, player, true)) {
                        return true;
                    } else if (choppingBlock.isEmpty()) {
                        if (choppingBlock.placeItem(slot, itemStack, false)) {
                            itemStack.stackSize--;

                            return true;
                        }
                    }
                }

                // Place tool when the workbench is empty
                if (isInput) {
                    if (choppingBlock.placeItem(slot, itemStack, true)) {
                        itemStack.stackSize--;

                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean retrieveItemFromChoppingBlockAt(EntityPlayer player, World world, int x, int y, int z,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityChoppingBlock choppingBlock = (TileEntityChoppingBlock) world.getTileEntity(x, y, z);
            int slot = getChoppingBlockSlotFromHit(choppingBlock, hitX, hitY, hitZ);
            if (slot != -1 && choppingBlock.retrieveItem(slot, player, true)) {
                return true;
            }
        }

        return false;

    }

    private static int getChoppingBlockSlotFromHit(TileEntityChoppingBlock choppingBlock,
            float hitX, float hitY, float hitZ) {
        final AxisAlignedBB near = AxisAlignedBB.getBoundingBox(hitX - 0.01f, hitY - 0.1f, hitZ - 0.01f,
                hitX + 0.01f, hitY + 0.1f, hitZ + 0.01f);

        for (int i = 0; i < TileEntityChoppingBlock.MAX_STORAGE; i++) {
            final AxisAlignedBB check = getChoppingBlockSlotBounds(i);

            if (check.intersectsWith(near)) {
                Bids.LOG.debug("Chopping block slot hit: " + i);

                return i;
            }
        }

        return -1;
    }

    public static boolean isLargeItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBlock;
    }

}
