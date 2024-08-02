package com.unforbidable.tfc.bids.Core.WoodPile;

import com.dunk.tfc.Blocks.Devices.BlockBarrel;
import com.dunk.tfc.Blocks.Devices.BlockHopper;
import com.dunk.tfc.TileEntities.TEBarrel;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockWoodPile;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;

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

                        if (player.capabilities.isCreativeMode) {
                            one.stackSize = 16;
                        }

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

    public static boolean isItemValidWoodPileItemForCharcoal(ItemStack itemStack) {
        // Item is allowed
        if (!(itemStack.getItem() == BidsItems.firewoodSeasoned
            || itemStack.getItem() == BidsItems.firewood && BidsOptions.WoodPile.allowCharcoalFromUnseasonedFirewood)) {
            return false;
        }

        // Wood type is flammable
        WoodIndex wood = WoodScheme.DEFAULT.findWood(itemStack);
        return !wood.inflammable;
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

    public static boolean canPlaceFireBlockAt(World world, int x, int y, int z) {
        // This is a clone of Blocks.fire.canPlaceBlockAt method
        // except the top surface of wood piles is never considered solid
        // for the fire to be placed on
        // The fire block will still be placed if any neighbor can burn
        return doesBlockHaveSolidTopSurfaceAndIsNotWoodPile(world, x, y - 1, z) || canFireBlockNeighborBurn(world, x, y, z);
    }

    private static boolean doesBlockHaveSolidTopSurfaceAndIsNotWoodPile(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return !(block instanceof BlockWoodPile) && block.isSideSolid(world, x, y, z, ForgeDirection.UP);
    }

    private static boolean canFireBlockNeighborBurn(World world, int x, int y, int z) {
        return Blocks.fire.canCatchFire(world, x + 1, y, z, WEST ) ||
            Blocks.fire.canCatchFire(world, x - 1, y, z, EAST ) ||
            Blocks.fire.canCatchFire(world, x, y - 1, z, UP   ) ||
            Blocks.fire.canCatchFire(world, x, y + 1, z, DOWN ) ||
            Blocks.fire.canCatchFire(world, x, y, z - 1, SOUTH) ||
            Blocks.fire.canCatchFire(world, x, y, z + 1, NORTH);
    }

    public static int offerPitchToHopper(World world, int x, int y, int z, int pitch) {
        BlockCoord bc = findHopperDestination(world, x, y, z);
        if (bc != null) {
            TileEntity bar = world.getTileEntity(bc.x, bc.y, bc.z);
            if (bar instanceof TEBarrel && ((TEBarrel) bar).canAcceptLiquids()) {
                TEBarrel barrel = (TEBarrel) bar;

                FluidStack fs = new FluidStack(TFCFluids.PITCH, pitch);
                if (barrel.addLiquid(fs)) {
                    // At least some pitch was accepted
                    return pitch - fs.amount;
                }
            }
        }

        return 0;
    }

    private static BlockCoord findHopperDestination(World world, int x, int y, int z) {
        List<BlockCoord> checked = new ArrayList<BlockCoord>();
        BlockCoord start = new BlockCoord(x, y, z);
        checked.add(start);
        return findHopperDestination(world, start, checked, 1);
    }

    private static BlockCoord findHopperDestination(World world, BlockCoord bc, List<BlockCoord> checked, int distance) {
        ForgeDirection dir = getHopperDirection(world, bc.x, bc.y, bc.z);
        BlockCoord next = bc.offset(dir.offsetX, dir.offsetY, dir.offsetZ);
        Block block = world.getBlock(next.x, next.y, next.z);
        if (block instanceof BlockBarrel) {
            Bids.LOG.info("Found barrel {} in distance {}", dir, distance);
            return next;
        } else {
            if (distance < 32 && block instanceof BlockHopper && !checked.contains(next)) {
                checked.add(next);
                Bids.LOG.info("Following hopper {}", dir);
                return findHopperDestination(world, next, checked, distance + 1);
            } else {
                return null;
            }
        }
    }

    private static ForgeDirection getHopperDirection(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        int dir = BlockHopper.getDirectionFromMetadata(meta);
        return ForgeDirection.VALID_DIRECTIONS[dir];
    }

}
