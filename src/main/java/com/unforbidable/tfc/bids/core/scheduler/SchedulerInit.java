package com.unforbidable.tfc.bids.core.scheduler;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class SchedulerInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new SchedulerHandler());
    }

}
