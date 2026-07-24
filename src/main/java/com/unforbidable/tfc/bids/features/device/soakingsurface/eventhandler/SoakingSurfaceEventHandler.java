package com.unforbidable.tfc.bids.features.device.soakingsurface.eventhandler;

import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.device.soakingsurface.main.SoakingSurfaceHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;

public class SoakingSurfaceEventHandler {

    @SubscribeEvent
    public void onSurfaceItemPlace(SurfaceItemEvent.Place event) {
        if (!event.placed && event.player.isSneaking() && event.face == 1) {
            if (SoakingSurfaceHelper.canPlaceSoakingItemAt(event.world, event.x, event.y, event.z, event.itemStack)) {
                if (!event.world.isRemote) {
                    ItemStack heldItem = event.player.getHeldItem().copy();
                    heldItem.stackSize = 1;

                    if (SoakingSurfaceHelper.placeSoakingItemAt(event.world, event.x, event.y, event.z, event.hitX, event.hitZ, heldItem)) {
                        event.player.getHeldItem().stackSize--;
                    }
                }

                event.placed = true;
            }

        }
    }

}
