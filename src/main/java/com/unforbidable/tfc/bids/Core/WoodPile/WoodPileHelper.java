package com.unforbidable.tfc.bids.Core.WoodPile;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WoodPileHelper {

    public static boolean createWoodPileAt(ItemStack itemStack, EntityPlayer player, World world,
            int x, int y, int z, int side) {
        if (!world.isRemote && player.isSneaking()
                && isItemValidWoodPileItem(itemStack)) {
            // Try to start a wood pile
            ForgeDirection d = ForgeDirection.getOrientation(side);
            int x2 = x + d.offsetX;
            int y2 = y + d.offsetY;
            int z2 = z + d.offsetZ;
            if (world.isAirBlock(x2, y2, z2)
                    && world.isSideSolid(x2 + ForgeDirection.DOWN.offsetX, y2 + ForgeDirection.DOWN.offsetY,
                            z2 + ForgeDirection.DOWN.offsetZ, ForgeDirection.UP)) {
                // The neighbor block in the direction of the side that was hit
                // must be air and block below needs to have solid side UP
                final int orientation = getWoodPileOrientation(player);
                world.setBlock(x2, y2, z2, BidsBlocks.woodPile, orientation, 2);
                TileEntity te = world.getTileEntity(x2, y2, z2);
                if (te instanceof TileEntityWoodPile) {
                    TileEntityWoodPile woodPile = (TileEntityWoodPile) te;
                    woodPile.setOrientation(orientation);
                    ItemStack one = itemStack.copy();
                    one.stackSize = 1;
                    if (woodPile.addItem(one)) {
                        itemStack.stackSize = itemStack.stackSize - 1;
                    }

                    return true;
                } else {
                    // This would happen in BlockWoodPile did not create a TileEntityWoodPile
                    // which is very bad
                    Bids.LOG.warn("Expected TileEntityWoodPile at " + x2 + ", " + y2 + ", " + z2);
                    world.setBlockToAir(x2, y2, z2);
                }
            }
        }

        return false;
    }

    public static boolean insertIntoWoodPileAt(ItemStack itemStack, EntityPlayer player, World world,
            int x, int y, int z) {
        if (!world.isRemote && isItemValidWoodPileItem(itemStack)) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityWoodPile) {
                TileEntityWoodPile woodPile = (TileEntityWoodPile) te;
                if (woodPile.addItem(itemStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean retrieveSelectedItemFromWoodPileAt(EntityPlayer player, World world, int x, int y, int z) {
        if (player.isSneaking() && player.getCurrentEquippedItem() == null) {
            // This is done on client side
            // as only the client has the selected item index
            if (world.isRemote) {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof TileEntityWoodPile) {
                    TileEntityWoodPile woodPile = (TileEntityWoodPile) te;

                    int index = woodPile.getSelectedItemIndex();
                    if (index != -1) {
                        // The item is retrieved server side
                        TileEntityWoodPile.sendRetrieveItem(world, x, y, z, index, player);
                    }

                }
            }

            // Still return true on server
            // to prevent GUI opening
            return true;
        }

        return false;
    }

    private static int getWoodPileOrientation(EntityPlayer player) {
        int dir = (int) Math.floor(player.rotationYaw * 4F / 360F + 0.5D);
        return dir & 3;
    }

    public static boolean isItemValidWoodPileItem(ItemStack itemStack) {
        return WoodPileRegistry.findItem(itemStack.getItem()) != null;
    }

    public static MovingObjectPosition onWoodPileCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec,
            Vec3 endVec) {
        TileEntityWoodPile te = (TileEntityWoodPile) world.getTileEntity(x, y, z);
        startVec = startVec.addVector(-x, -y, -z);
        endVec = endVec.addVector(-x, -y, -z);

        WoodPileItemCollision nearestCol = null;
        WoodPileItemBounds nearestItem = null;

        for (WoodPileItemBounds item : te.getItemBounds()) {
            WoodPileItemCollision col = rayTraceAABB(item.getBounds(), startVec, endVec);

            // When the item collides
            // Save if first or closer than the nearest so far
            if (col != null && (nearestCol == null || col.distance < nearestCol.distance)) {
                nearestCol = col;
                nearestItem = item;
            }
        }

        if (nearestItem != null) {
            te.setSelectedItemIndex(nearestItem.getIndex());
        } else {
            te.setSelectedItemIndex(-1);
        }

        return null;
    }

    // TODO: This almost identical to CarvingHelper.rayTraceAABB
    // so both should use the same code
    private static WoodPileItemCollision rayTraceAABB(AxisAlignedBB bound, Vec3 startVec, Vec3 endVec) {
        Vec3 minX = startVec.getIntermediateWithXValue(endVec, bound.minX);
        Vec3 maxX = startVec.getIntermediateWithXValue(endVec, bound.maxX);
        Vec3 minY = startVec.getIntermediateWithYValue(endVec, bound.minY);
        Vec3 maxY = startVec.getIntermediateWithYValue(endVec, bound.maxY);
        Vec3 minZ = startVec.getIntermediateWithZValue(endVec, bound.minZ);
        Vec3 maxZ = startVec.getIntermediateWithZValue(endVec, bound.maxZ);

        if (!isVecInsideYZBounds(bound, minX))
            minX = null;
        if (!isVecInsideYZBounds(bound, maxX))
            maxX = null;
        if (!isVecInsideXZBounds(bound, minY))
            minY = null;
        if (!isVecInsideXZBounds(bound, maxY))
            maxY = null;
        if (!isVecInsideXYBounds(bound, minZ))
            minZ = null;
        if (!isVecInsideXYBounds(bound, maxZ))
            maxZ = null;

        Vec3 hitVec = null;
        if (minX != null && (hitVec == null || startVec.distanceTo(minX) < startVec.distanceTo(hitVec)))
            hitVec = minX;
        if (maxX != null && (hitVec == null || startVec.distanceTo(maxX) < startVec.distanceTo(hitVec)))
            hitVec = maxX;
        if (minY != null && (hitVec == null || startVec.distanceTo(minY) < startVec.distanceTo(hitVec)))
            hitVec = minY;
        if (maxY != null && (hitVec == null || startVec.distanceTo(maxY) < startVec.distanceTo(hitVec)))
            hitVec = maxY;
        if (minZ != null && (hitVec == null || startVec.distanceTo(minZ) < startVec.distanceTo(hitVec)))
            hitVec = minZ;
        if (maxZ != null && (hitVec == null || startVec.distanceTo(maxZ) < startVec.distanceTo(hitVec)))
            hitVec = maxZ;

        if (hitVec == null)
            return null;

        int side = -1;
        if (hitVec == minX)
            side = 4;
        if (hitVec == maxX)
            side = 5;
        if (hitVec == minY)
            side = 0;
        if (hitVec == maxY)
            side = 1;
        if (hitVec == minZ)
            side = 2;
        if (hitVec == maxZ)
            side = 3;

        return new WoodPileItemCollision(hitVec, side, startVec.distanceTo(hitVec));
    }

    private static boolean isVecInsideYZBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.yCoord >= bound.minY && vec3.yCoord <= bound.maxY && vec3.zCoord >= bound.minZ
                    && vec3.zCoord <= bound.maxZ;
    }

    private static boolean isVecInsideXZBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.xCoord >= bound.minX && vec3.xCoord <= bound.maxX && vec3.zCoord >= bound.minZ
                    && vec3.zCoord <= bound.maxZ;
    }

    private static boolean isVecInsideXYBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.xCoord >= bound.minX && vec3.xCoord <= bound.maxX && vec3.yCoord >= bound.minY
                    && vec3.yCoord <= bound.maxY;
    }

}
