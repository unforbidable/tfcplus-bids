package com.unforbidable.tfc.bids.features.device.strawnest.tileentity;

import com.dunk.tfc.TileEntities.TENestBox;
import com.unforbidable.tfc.bids.api.features.strawnest.Nest;

public class TileEntityStrawNest extends TENestBox implements Nest {

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public String getInventoryName() {
        return "StrawNest";
    }

}
