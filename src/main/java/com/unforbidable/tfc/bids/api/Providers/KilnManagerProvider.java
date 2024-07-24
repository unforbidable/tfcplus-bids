package com.unforbidable.tfc.bids.api.Providers;

import com.unforbidable.tfc.bids.Core.Kilns.KilnManager;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnManager;

public class KilnManagerProvider {

    public static IKilnManager getKilnManager(IKilnHeatSource heatSource) {
        return new KilnManager(heatSource);
    }

}
