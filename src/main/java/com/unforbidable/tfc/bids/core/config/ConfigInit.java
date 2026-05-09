package com.unforbidable.tfc.bids.core.config;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ConfigHandler(event.getModConfigurationDirectory()));
    }

}
