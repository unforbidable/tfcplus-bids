package com.unforbidable.tfc.bids.compat.tfc;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.eventhandler.WorldEventHandler;
import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.player.network.InitClientPacket;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class TfcInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        TfcSetup.setupDrinks();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        TfcSetup.setupOres();
        TfcSetup.setupRecipes();
        TfcSetup.setupBarrelRecipes();
        TfcSetup.setupGlassblowing();
        TfcSetup.setupCarving();
        TfcSetup.setupYeastFood();

        TfcRegistryHelper.registerCommon();

        // Anvil recipes are registered when world loads
        // ideally after TFC initialized its AnvilManager
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Network.handlePacket(InitClientPacket.class, c -> {
            Bids.LOG.info("Client received message to initialize world-bound recipes");

            TfcRegistryHelper.registerWorldLoad();
        });
    }

}
