package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.Core.BlockSetup;
import com.unforbidable.tfc.bids.Core.ItemSetup;
import com.unforbidable.tfc.bids.Core.KeyBindingSetup;
import com.unforbidable.tfc.bids.Handlers.Client.*;
import com.unforbidable.tfc.bids.NEI.NotEnoughItemsSetup;
import com.unforbidable.tfc.bids.WAILA.WailaSetup;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        FMLCommonHandler.instance().bus().register(new ClientTickHandler());

        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new ClientGuiHandler());
        MinecraftForge.EVENT_BUS.register(new ClientGuiHandler());
        MinecraftForge.EVENT_BUS.register(new FarmlandHighlightHandler());
        MinecraftForge.EVENT_BUS.register(new PlacementHighlightHandler());

        BlockSetup.preInitClientOnly();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent event) {
        super.init(event);

        if (Loader.isModLoaded("Waila")) {
            WailaSetup.init();
        }

        if (Loader.isModLoaded("NotEnoughItems")) {
            NotEnoughItemsSetup.init();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        ItemSetup.postInitClientOnly();

        KeyBindingSetup.postInit();

        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
        MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());
    }

}
