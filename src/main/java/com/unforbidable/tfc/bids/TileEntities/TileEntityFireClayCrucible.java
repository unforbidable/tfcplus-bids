package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.api.BidsGui;

public class TileEntityFireClayCrucible extends TileEntityCrucible {

    @Override
    public int getGui() {
        return BidsGui.fireClayCrucibleGui;
    }

    @Override
    public int getInputSlotCount() {
        return 12;
    }

    @Override
    public String getInventoryName() {
        return "Crucible";
    }

    @Override
    public int getMaxVolume() {
        return 3000;
    }

    @Override
    public int getMaxTemp() {
        return 1800;
    }

    @Override
    public boolean hasLiquidInputSlot() {
        return true;
    }

}
