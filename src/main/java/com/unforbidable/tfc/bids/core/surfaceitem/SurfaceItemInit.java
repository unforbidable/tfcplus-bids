package com.unforbidable.tfc.bids.core.surfaceitem;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class SurfaceItemInit extends Initializable {

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SurfaceItemHandler());
    }

}
