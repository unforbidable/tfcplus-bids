package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.Core.BlockSetup;
import com.unforbidable.tfc.bids.Handlers.Client.ClientGuiHandler;
import com.unforbidable.tfc.bids.WAILA.WailaSetup;

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

        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new ClientGuiHandler());
        MinecraftForge.EVENT_BUS.register(new ClientGuiHandler());

        BlockSetup.preInitClientOnly();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent event) {
        super.init(event);

        WailaSetup.init();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}
