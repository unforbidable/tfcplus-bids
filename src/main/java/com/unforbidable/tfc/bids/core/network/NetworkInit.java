package com.unforbidable.tfc.bids.core.network;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.network.container.ContainerMessage;
import com.unforbidable.tfc.bids.core.network.container.ContainerMessageHandler;
import com.unforbidable.tfc.bids.core.network.tileentity.TileEntityMessage;
import com.unforbidable.tfc.bids.core.network.tileentity.TileEntityMessageHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;

public class NetworkInit extends Initializable {

    @Override
    public void init(FMLInitializationEvent event) {
        Bids.LOG.info("Register common network packets");

        Network.registerPacket(SimpleUpdatePacket.class);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Bids.LOG.info("Register network message");

        Bids.network.registerMessage(NetworkMessageHandler.class, NetworkMessage.class, 0, Side.CLIENT);
        Bids.network.registerMessage(NetworkMessageHandler.class, NetworkMessage.class, 1, Side.SERVER);
        Bids.network.registerMessage(TileEntityMessageHandler.class, TileEntityMessage.class, 2, Side.CLIENT);
        Bids.network.registerMessage(TileEntityMessageHandler.class, TileEntityMessage.class, 3, Side.SERVER);
        Bids.network.registerMessage(ContainerMessageHandler.class, ContainerMessage.class, 4, Side.CLIENT);
        Bids.network.registerMessage(ContainerMessageHandler.class, ContainerMessage.class, 5, Side.SERVER);
    }

}
