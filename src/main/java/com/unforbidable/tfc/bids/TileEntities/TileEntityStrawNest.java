package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.TileEntities.TENestBox;
import com.unforbidable.tfc.bids.api.Interfaces.INest;

public class TileEntityStrawNest extends TENestBox implements INest {

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public String getInventoryName() {
        return "StrawNest";
    }

}
