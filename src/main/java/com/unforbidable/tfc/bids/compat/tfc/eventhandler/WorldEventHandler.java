package com.unforbidable.tfc.bids.compat.tfc.eventhandler;

import com.unforbidable.tfc.bids.compat.tfc.TfcRegistryHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldEventHandler {

    @SubscribeEvent
    public void onLoadWorld(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            TfcRegistryHelper.registerWorldLoad();
        }
    }


}
