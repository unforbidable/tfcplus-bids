package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.core.Initializer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Initializer.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        Initializer.init(event);
    }

    public void postInit(FMLPostInitializationEvent event) {
        Initializer.postInit(event);
    }

}
