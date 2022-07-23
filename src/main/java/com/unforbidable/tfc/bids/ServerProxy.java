package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.Handlers.WorldEventHandler;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

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

        // Anvil recipes are registered when world loads
        // ideally after TFC initialized its AnvilManager
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }

}
