package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.core.Initializer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Initializer.preInit(event);

//        FMLCommonHandler.instance().bus().register(new ConfigHandler(event.getModConfigurationDirectory()));
//
//        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new GuiHandler());
//
//        ItemSetup.preInit();
//        BlockSetup.preInit();
//        FluidSetup.preInit();
//        DrinkSetup.preInit();
//        CropSetup.preInit();
//        WoodSetup.preInit();
//        StoneSetup.preInit();
//        OreSetup.preInit();
//        WoodworkingSetup.preInit();
//        NetworkSetup.preInit();
    }

    public void init(FMLInitializationEvent event) {
        Initializer.init(event);

//        RecipeSetup.init();
//        WoodworkingSetup.init();
//        AchievementSetup.init();
//
//        MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
//        MinecraftForge.EVENT_BUS.register(new SurfaceItemHandler());
//        MinecraftForge.EVENT_BUS.register(new WoodPileHandler());
//        MinecraftForge.EVENT_BUS.register(new DryingRackHandler());
//        MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());
//        MinecraftForge.EVENT_BUS.register(new LivingDropsEventHandler());
//        MinecraftForge.EVENT_BUS.register(new EntitySpawnHandler());
//        MinecraftForge.EVENT_BUS.register(new AchievementHandler());
//        MinecraftForge.EVENT_BUS.register(new FireSettingHandler());
//        MinecraftForge.EVENT_BUS.register(new KilnPotteryFiringHandler());
//        MinecraftForge.EVENT_BUS.register(new KilnWoodDryingHandler());
//        MinecraftForge.EVENT_BUS.register(new AnimalMilkHandler());
//        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
//        MinecraftForge.EVENT_BUS.register(new WoodworkingHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        Initializer.postInit(event);

//        ItemSetup.postInit();
//        BlockSetup.postInit();
//        DrinkSetup.postInit();
//        RecipeSetup.postInit();
//
//        FMLCommonHandler.instance().bus().register(new PlayerTracker());
//
//        // Anvil recipes are registered when world loads
//        // ideally after TFC initialized its AnvilManager
//        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }

}
