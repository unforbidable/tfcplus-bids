package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.core.Initializer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

    @Override
    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        Initializer.preInitClientOnly(event);

//        FMLCommonHandler.instance().bus().register(new ClientTickHandler());
//
//        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new ClientGuiHandler());
//        MinecraftForge.EVENT_BUS.register(new ClientGuiHandler());
//        MinecraftForge.EVENT_BUS.register(new FarmlandHighlightHandler());
//        MinecraftForge.EVENT_BUS.register(new PlacementHighlightHandler());
//        MinecraftForge.EVENT_BUS.register(new AdzeHighlightHandler());
//
//        BlockSetup.preInitClientOnly();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent event) {
        super.init(event);

        Initializer.initClientOnly(event);

//        if (Loader.isModLoaded("Waila")) {
//            WailaSetup.init();
//        }
//
//        if (Loader.isModLoaded("NotEnoughItems")) {
//            NotEnoughItemsSetup.init();
//        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        Initializer.postInitClientOnly(event);


//        ItemSetup.postInitClientOnly();
//
//        KeyBindingSetup.postInit();
//
//        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
//        MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());

    }

}
