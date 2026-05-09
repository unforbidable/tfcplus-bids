package com.unforbidable.tfc.bids.core.player;

import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.player.network.InitClientPacket;
import com.unforbidable.tfc.bids.core.player.event.PlayerLoggedInEventHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PlayerInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Network.registerPacket(InitClientPacket.class);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new PlayerLoggedInEventHandler());
    }

}
