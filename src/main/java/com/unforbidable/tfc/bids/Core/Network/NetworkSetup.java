package com.unforbidable.tfc.bids.Core.Network;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.Messages.FillContainerFromWorldMessage;
import com.unforbidable.tfc.bids.Core.Network.Messages.InitClientMessage;

import cpw.mods.fml.relauncher.Side;

public class NetworkSetup {

    public static void preInit() {
        Bids.network.registerMessage(InitClientMessage.ServerHandler.class, InitClientMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(InitClientMessage.ClientHandler.class, InitClientMessage.class,
                NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);

        Bids.network.registerMessage(FillContainerFromWorldMessage.ServerHandler.class, FillContainerFromWorldMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.SERVER);
        Bids.network.registerMessage(FillContainerFromWorldMessage.ClientHandler.class, FillContainerFromWorldMessage.class,
            NetworkHelper.getNextAvailableMessageId(), Side.CLIENT);
    }

}
