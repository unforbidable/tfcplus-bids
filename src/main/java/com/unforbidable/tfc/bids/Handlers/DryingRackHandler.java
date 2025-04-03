package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class DryingRackHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (event.face > 1) {
                    if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == TFCItems.pole && event.entityPlayer.getHeldItem().stackSize > 1) {
                        ForgeDirection dir = ForgeDirection.getOrientation(event.face);
                        if (DryingRackHelper.canPlaceDryingRackAt(event.world, event.x, event.y, event.z, dir)) {
                            DryingRackHelper.placeCordlessDryingRackAt(event.entityPlayer.getHeldItem(), event.entityPlayer,
                                event.world, event.x, event.y, event.z, dir);

                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

}
