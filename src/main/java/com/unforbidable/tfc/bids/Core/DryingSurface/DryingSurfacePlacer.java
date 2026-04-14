package com.unforbidable.tfc.bids.Core.DryingSurface;

import com.unforbidable.tfc.bids.api.Interfaces.ISurfaceItemPlacer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DryingSurfacePlacer implements ISurfaceItemPlacer {

    @Override
    public boolean placeItemOnSurface(World world, int x, int y, int z, int face, EntityPlayer player) {
        if (player.isSneaking() && face == 1) {
            if (DryingSurfaceHelper.canPlaceDryingItemAt(world, x, y, z, player.getHeldItem())) {
                if (!world.isRemote) {
                    ItemStack heldItem = player.getHeldItem().copy();
                    heldItem.stackSize = 1;

                    if (DryingSurfaceHelper.placeDryingItemAt(world, x, y, z, heldItem)) {
                        player.getHeldItem().stackSize--;
                    }
                }

                return true;
            }

        }

        return false;
    }

}
