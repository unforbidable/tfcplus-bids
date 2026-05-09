package com.unforbidable.tfc.bids.core.gui;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.gui.client.ClientGuiHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new GuiHandler());
    }

    @Override
    public void preInitClientOnly(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new ClientGuiHandler());
    }

}
