package com.unforbidable.tfc.bids.features.crafting.threshing.eventhandler;

import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.crafting.threshing.main.ThreshingHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ThreshingEventHandler {

    @SubscribeEvent
    public void onSurfaceItem(SurfaceItemEvent.Place event) {
        if (!event.placed && !event.player.isSneaking() && event.face == 1) {
            if (ThreshingHelper.thresh(event.world, event.x, event.y, event.z, event.player, event.itemStack)) {
                event.placed = true;
            }
        }
    }

}
