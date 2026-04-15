package com.unforbidable.tfc.bids.Core.SoakingSurface;

import com.unforbidable.tfc.bids.api.Interfaces.ISurfaceItemPlacer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SoakingSurfacePlacer implements ISurfaceItemPlacer {

    @Override
    public boolean placeItemOnSurface(World world, int x, int y, int z, int face, float hitX, float hitY, float hitZ, EntityPlayer player) {
        if (player.isSneaking() && face == 1) {
            if (SoakingSurfaceHelper.canPlaceSoakingItemAt(world, x, y, z, player.getHeldItem())) {
                if (!world.isRemote) {
                    ItemStack heldItem = player.getHeldItem().copy();
                    heldItem.stackSize = 1;

                    if (SoakingSurfaceHelper.placeSoakingItemAt(world, x, y, z, hitX, hitZ, heldItem)) {
                        player.getHeldItem().stackSize--;
                    }
                }

                return true;
            }

        }

        return false;
    }

}
