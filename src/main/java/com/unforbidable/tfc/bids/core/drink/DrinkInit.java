package com.unforbidable.tfc.bids.core.drink;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class DrinkInit extends Initializable {

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        // TODO check IDs are unique

        // Register fluid containers based on registered drinks
        Bids.LOG.info("Register drink fluid containers");
        DrinkRegistry.vessels.stream()
            .forEach(DrinkUtil::registerVessel);
    }

}
