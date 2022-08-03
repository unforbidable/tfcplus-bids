package com.unforbidable.tfc.bids.Core.WoodPile;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
                    world.markBlockForUpdate(x, y, z);
                    return true;
                }
            }
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

}
