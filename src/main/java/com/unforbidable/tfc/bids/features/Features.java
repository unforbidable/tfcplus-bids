package com.unforbidable.tfc.bids.features;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.features.device.crucible.Crucible;
import com.unforbidable.tfc.bids.features.material.ore.Ore;
import com.unforbidable.tfc.bids.features.material.pottery.Pottery;

public class Features {

    public static final Feature[] features = {
        new Ore(),
        new Crucible(),
        new Pottery(),
    };

}
