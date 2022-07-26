package com.unforbidable.tfc.bids.Core.Player;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.Messages.InitClientMessage;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerTracker {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            Bids.network.sendTo(new InitClientMessage(), (EntityPlayerMP) event.player);
            Bids.LOG.info("Sending init client mesage");
        }
    }

}
