package com.unforbidable.tfc.bids.features.device.cookingpit.eventhandler;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CookingPitEventHandler {

    @SubscribeEvent
    public void onSurfaceItem(SurfaceItemEvent.Place event) {
        if (!event.placed && event.player.isSneaking()) {
            if (event.itemStack.getItem() == TFCItems.clayBall && event.itemStack.getItemDamage() == 0 && event.itemStack.stackSize >= 25) {
                if (CookingPitHelper.placeClayCookingPitAt(event.world, event.x, event.y, event.z, event.face, event.player)) {
                    event.itemStack.stackSize -= 25;

                    event.placed = true;
                }
            }
        }
    }

}
