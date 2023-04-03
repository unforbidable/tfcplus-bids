package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.Core.*;
import com.unforbidable.tfc.bids.Core.Network.NetworkSetup;
import com.unforbidable.tfc.bids.Core.Player.PlayerTracker;
import com.unforbidable.tfc.bids.Handlers.Client.KeyBindingHandler;
import com.unforbidable.tfc.bids.Handlers.ConfigHandler;
import com.unforbidable.tfc.bids.Handlers.GuiHandler;
import com.unforbidable.tfc.bids.Handlers.WorldEventHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ConfigHandler(event.getModConfigurationDirectory()));

        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new GuiHandler());

        ItemSetup.preInit();
        BlockSetup.preInit();
        DrinkSetup.preInit();
        NetworkSetup.preInit();
    }

    public void init(FMLInitializationEvent event) {
        RecipeSetup.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        ItemSetup.postInit();
        BlockSetup.postInit();
        DrinkSetup.postInit();
        RecipeSetup.postInit();
        KeyBindingSetup.postInit();

        FMLCommonHandler.instance().bus().register(new PlayerTracker());
        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());

        // Anvil recipes are registered when world loads
        // ideally after TFC initialized its AnvilManager
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }

}
