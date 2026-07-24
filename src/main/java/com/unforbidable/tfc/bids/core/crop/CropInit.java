package com.unforbidable.tfc.bids.core.crop;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class CropInit extends Initializable {

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        for (BidsCropIndex crop : CropRegistry.crops) {
            BidsCropManager.registerCrop(crop);
        }
    }

}
