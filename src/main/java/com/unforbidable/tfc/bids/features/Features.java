package com.unforbidable.tfc.bids.features;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.features.building.mudbrick.Mudbrick;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.Glassblowing;
import com.unforbidable.tfc.bids.features.device.crucible.Crucible;
import com.unforbidable.tfc.bids.features.material.glass.Glass;
import com.unforbidable.tfc.bids.features.material.ore.Ore;
import com.unforbidable.tfc.bids.features.material.pottery.Pottery;
import com.unforbidable.tfc.bids.features.utility.adze.Adze;
import com.unforbidable.tfc.bids.features.utility.drill.Drill;

public class Features {

    public static final Feature[] features = {
        new Ore(),
        new Crucible(),
        new Pottery(),
        new Mudbrick(),
        new Glassblowing(),
        new Glass(),
        new Drill(),
        new Adze(),
    };

}
