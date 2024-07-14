package com.unforbidable.tfc.bids.Core.WoodPile;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WoodPileHelper {

    public static boolean createWoodPileAt(ItemStack itemStack, EntityPlayer player, World world,
            int x, int y, int z, int side) {
        if (!world.isRemote && player.isSneaking()) {
            // Try to start a wood pile
            ForgeDirection d = ForgeDirection.getOrientation(side);
            int x2 = x + d.offsetX;
            int y2 = y + d.offsetY;
            int z2 = z + d.offsetZ;
            if (canCreateWoodPileAt(world, x2, y2, z2)) {
                // The neighbor block in the direction of the side that was hit
                // must allow placing woodpile on top
                final int orientation = getWoodPileOrientation(player);
                world.setBlock(x2, y2, z2, BidsBlocks.woodPile, orientation, 3);
                TileEntity te = world.getTileEntity(x2, y2, z2);
                if (te instanceof TileEntityWoodPile) {
                    TileEntityWoodPile woodPile = (TileEntityWoodPile) te;
                    woodPile.setOrientation(orientation);

                    if (isItemValidWoodPileItem(itemStack)) {
                        ItemStack one = itemStack.copy();
                        one.stackSize = 1;
                        if (woodPile.addItem(one)) {
                            itemStack.stackSize = itemStack.stackSize - 1;
                        }
                    } else {
                        // If item cannot be added open the GUI instead
                        woodPile.openDelayedGUI(player);
                    }

                    // Catch fire from fire blocks nearby after placing
                    woodPile.tryToCatchFire();

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

    private static boolean canCreateWoodPileAt(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z) || world.getBlock(x, y, z) == Blocks.fire) {
            TileEntity te = world.getTileEntity(x, y - 1, z);
            if (te instanceof TileEntityWoodPile) {
                // Either a wood pile that is full
                return ((TileEntityWoodPile) te).isFull();
            } else {
                // Or the block below has solid top side
                return world.isSideSolid(x, y - 1, z, ForgeDirection.UP);
            }
        } else {
            return false;
        }
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

        CollisionInfo nearestCol = null;
        WoodPileItemBounds nearestItem = null;

        for (WoodPileItemBounds item : te.getItemBounds()) {
            CollisionInfo col = CollisionHelper.rayTraceAABB(item.getBounds(), startVec, endVec);

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

}
