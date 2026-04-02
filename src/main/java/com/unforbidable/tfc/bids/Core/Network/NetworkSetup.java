package com.unforbidable.tfc.bids.Core.Network;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.Messages.InitClientMessage;
import com.unforbidable.tfc.bids.Core.Player.PlayerStatsMessage;
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
