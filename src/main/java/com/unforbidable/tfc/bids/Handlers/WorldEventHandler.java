package com.unforbidable.tfc.bids.Handlers;

import com.unforbidable.tfc.bids.Core.RecipeSetup;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldEventHandler {

    @SubscribeEvent
    public void onLoadWorld(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            RecipeSetup.onWorldLoad();
        }
    }

}
