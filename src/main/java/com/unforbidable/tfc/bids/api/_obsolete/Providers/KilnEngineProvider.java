package com.unforbidable.tfc.bids.api._obsolete.Providers;

import com.unforbidable.tfc.bids.features.device.kiln.main.engine.DefaultKilnEngine;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IKilnHeatSource;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.KilnEngine;

public class KilnEngineProvider {

    public static KilnEngine getKilnManager(IKilnHeatSource heatSource) {
        return new DefaultKilnEngine(heatSource);
    }

}
