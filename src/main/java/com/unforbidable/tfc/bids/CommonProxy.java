package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.Core.*;
import com.unforbidable.tfc.bids.Core.Network.NetworkSetup;
import com.unforbidable.tfc.bids.Core.Player.PlayerTracker;
import com.unforbidable.tfc.bids.Handlers.*;

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
        FluidSetup.preInit();
        DrinkSetup.preInit();
        CropSetup.preInit();
        WoodSetup.preInit();
        StoneSetup.preInit();
        NetworkSetup.preInit();
    }

    public void init(FMLInitializationEvent event) {
        RecipeSetup.init();
        AchievementSetup.init();

        MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
        MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());
        MinecraftForge.EVENT_BUS.register(new LivingDropsEventHandler());
        MinecraftForge.EVENT_BUS.register(new EntitySpawnHandler());
        MinecraftForge.EVENT_BUS.register(new AchievementHandler());
        MinecraftForge.EVENT_BUS.register(new FireSettingHandler());
        MinecraftForge.EVENT_BUS.register(new KilnPotteryFiringHandler());
        MinecraftForge.EVENT_BUS.register(new KilnWoodDryingHandler());
        MinecraftForge.EVENT_BUS.register(new AnimalMilkHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        ItemSetup.postInit();
        BlockSetup.postInit();
        DrinkSetup.postInit();
        RecipeSetup.postInit();

        FMLCommonHandler.instance().bus().register(new PlayerTracker());

        // Anvil recipes are registered when world loads
        // ideally after TFC initialized its AnvilManager
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }

}
