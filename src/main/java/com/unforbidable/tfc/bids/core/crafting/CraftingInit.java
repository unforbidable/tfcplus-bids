package com.unforbidable.tfc.bids.core.crafting;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CraftingInit extends Initializable {

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new CraftingHandler());
    }
}
