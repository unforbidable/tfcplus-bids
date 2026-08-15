package com.unforbidable.tfc.bids.features.device.dryingrack.eventhandler;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.util.ForgeDirection;

public class DryingRackEventHandler {

    @SubscribeEvent
    public void onSurfaceItem(SurfaceItemEvent.Place event) {
        if (!event.placed && event.face > 1 && event.player.isSneaking()) {
            if (event.itemStack.getItem() == TFCItems.pole && event.itemStack.stackSize > 1) {
                ForgeDirection dir = ForgeDirection.getOrientation(event.face);
                if (DryingRackHelper.canPlaceDryingRackAt(event.world, event.x, event.y, event.z, dir)) {
                    if (!event.world.isRemote) {
                        DryingRackHelper.placeDryingRackFromItemsAt(event.itemStack, event.player, event.world, event.x, event.y, event.z, dir);
                    }

                    event.placed = true;
                }
            }
        }
    }

}
