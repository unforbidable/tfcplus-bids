package com.unforbidable.tfc.bids.compat.nei;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NeiInit extends Initializable {

    @SideOnly(Side.CLIENT)
    @Override
    public void initClientOnly(FMLInitializationEvent event) {
        NeiSetup.registerHandlers();
        NeiSetup.hideItemStacks();
    }

}
