package com.unforbidable.tfc.bids.Handlers.Client;

import com.unforbidable.tfc.bids.Core.Carving.CarvingHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;

public class ClientTickHandler {

    @SubscribeEvent
    public void onClientPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            EntityPlayer player = event.player;

            CarvingHelper.trackPlayerCarvingActivity(player);
        }
    }

}
