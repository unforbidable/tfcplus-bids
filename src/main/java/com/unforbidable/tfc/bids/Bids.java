package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.WorldGen.AquiferWorldGen;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class Bids {

    public static final Logger LOG = LogManager.getLogger(Tags.MOD_NAME);

    public static SimpleNetworkWrapper network;

    @SidedProxy(clientSide = Tags.PACKAGE + ".ClientProxy", serverSide = Tags.PACKAGE + ".ServerProxy")
    public static CommonProxy proxy;

    @Instance(Tags.MOD_ID)
    public static Bids instance;

    public Bids() {
    }

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("BidsChannel");

        GameRegistry.registerWorldGenerator(new AquiferWorldGen(), 0);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about.
    // Register recipes."
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on
    // this."
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
