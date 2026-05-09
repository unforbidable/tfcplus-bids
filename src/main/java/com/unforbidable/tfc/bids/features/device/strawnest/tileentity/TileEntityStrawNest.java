package com.unforbidable.tfc.bids.features.device.strawnest.tileentity;

import com.dunk.tfc.TileEntities.TENestBox;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.INest;

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
