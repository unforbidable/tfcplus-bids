package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FeatureScannerInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        FeatureScanner.discoverFeatures(event.getAsmData());
    }

}
