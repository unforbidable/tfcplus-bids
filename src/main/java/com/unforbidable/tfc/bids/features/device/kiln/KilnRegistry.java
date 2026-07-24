package com.unforbidable.tfc.bids.features.device.kiln;

import com.unforbidable.tfc.bids.api.features.kiln.KilnChamber;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

public class KilnRegistry {

    public final static ListRegistry<Class<? extends KilnChamber>> chambers = new ListRegistry<>();

}
