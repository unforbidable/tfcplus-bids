package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.Core.BlockSetup;
import com.unforbidable.tfc.bids.Core.DrinkSetup;
import com.unforbidable.tfc.bids.Core.ItemSetup;
import com.unforbidable.tfc.bids.Core.RecipeSetup;
import com.unforbidable.tfc.bids.Handlers.ConfigHandler;
import com.unforbidable.tfc.bids.Handlers.GuiHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ConfigHandler(event.getModConfigurationDirectory()));

        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new GuiHandler());

        ItemSetup.preInit();
        BlockSetup.preInit();
        DrinkSetup.preInit();
    }

    public void init(FMLInitializationEvent event) {
        RecipeSetup.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        ItemSetup.postInit();
        BlockSetup.postInit();
        DrinkSetup.postInit();
        RecipeSetup.postInit();
    }

}
