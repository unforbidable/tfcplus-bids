package com.unforbidable.tfc.bids.core.network._obsolete;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.player._obsolete.PlayerStatsMessage;
import cpw.mods.fml.relauncher.Side;

public class NetworkSetup {

    public static void preInit() {
        Bids.network.registerMessage(InitClientMessage.ServerHandler.class, InitClientMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(InitClientMessage.ClientHandler.class, InitClientMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);

        Bids.network.registerMessage(PlayerStatsMessage.ServerHandler.class, PlayerStatsMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(PlayerStatsMessage.ClientHandler.class, PlayerStatsMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

}
