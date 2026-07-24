package com.unforbidable.tfc.bids.compat.waila;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaEntityProvider;
import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WailaInit extends Initializable {

    @SideOnly(Side.CLIENT)
    @Override
    public void initClientOnly(FMLInitializationEvent event) {
        Bids.LOG.info("Register WAILA setup callback");

        FMLInterModComms.sendMessage("Waila", "register", WailaDataProvider.class.getCanonicalName() + ".setup");
        FMLInterModComms.sendMessage("Waila", "register", WailaEntityProvider.class.getCanonicalName() + ".setup");
    }

}
