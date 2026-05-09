package com.unforbidable.tfc.bids.core.player.event;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.player.network.InitClientPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class PlayerLoggedInEventHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            Bids.LOG.info("Player has logged in - sending init client packet");

            Network.sendToClient(new InitClientPacket(), event.player);
        }
    }

}
