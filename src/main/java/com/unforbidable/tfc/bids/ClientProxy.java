package com.unforbidable.tfc.bids;

import com.unforbidable.tfc.bids.Handlers.Client.ClientGuiHandler;
import com.unforbidable.tfc.bids.Render.Blocks.RenderClayCrucible;
import com.unforbidable.tfc.bids.Render.Blocks.RenderFireClayCrucible;
import com.unforbidable.tfc.bids.Render.Blocks.RenderQuarry;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import cpw.mods.fml.client.registry.RenderingRegistry;
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

        RenderingRegistry.registerBlockHandler(
                BidsBlocks.clayCrucibleRenderId = RenderingRegistry.getNextAvailableRenderId(),
                new RenderClayCrucible());
        RenderingRegistry.registerBlockHandler(
                BidsBlocks.fireClayCrucibleRenderId = RenderingRegistry.getNextAvailableRenderId(),
                new RenderFireClayCrucible());
        RenderingRegistry.registerBlockHandler(
                BidsBlocks.quarryRenderId = RenderingRegistry.getNextAvailableRenderId(),
                new RenderQuarry());

        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new ClientGuiHandler());
        MinecraftForge.EVENT_BUS.register(new ClientGuiHandler());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}
