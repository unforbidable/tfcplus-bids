package com.unforbidable.tfc.bids;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ServerProxy extends CommonProxy {

    @Override
    @SideOnly(Side.SERVER)
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

    }

    @Override
    @SideOnly(Side.SERVER)
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}
