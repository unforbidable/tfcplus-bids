package com.unforbidable.tfc.bids.core.schemes;

import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneSetup;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodSetup;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class SchemeInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        StoneSetup.registerStone();
        WoodSetup.registerWood();
    }

}
