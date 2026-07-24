package com.unforbidable.tfc.bids.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public abstract class Initializable {

    public void preInit(FMLPreInitializationEvent event) {}

    public void preInitClientOnly(FMLPreInitializationEvent event) {}

    public void init(FMLInitializationEvent event) {}

    public void initClientOnly(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    public void postInitClientOnly(FMLPostInitializationEvent event) {}

}
