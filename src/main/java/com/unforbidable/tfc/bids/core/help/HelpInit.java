package com.unforbidable.tfc.bids.core.help;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class HelpInit extends Initializable {

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new HelpTooltipEventHandler());
    }

}
