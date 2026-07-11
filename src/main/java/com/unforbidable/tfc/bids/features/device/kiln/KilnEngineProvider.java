package com.unforbidable.tfc.bids.features.device.kiln;

import com.unforbidable.tfc.bids.features.device.kiln.main.engine.DefaultKilnEngine;
import com.unforbidable.tfc.bids.api.features.kiln.KilnHeatSource;
import com.unforbidable.tfc.bids.api.features.kiln.KilnEngine;

public class KilnEngineProvider {

    public static KilnEngine getKilnManager(KilnHeatSource heatSource) {
        return new DefaultKilnEngine(heatSource);
    }

}
