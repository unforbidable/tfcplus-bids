package com.unforbidable.tfc.bids.features.device.crucible.tileentity;

import com.unforbidable.tfc.bids.api._obsolete.BidsGui;

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
    public float getHeatTransferEfficiency() {
        return 1;
    }

    @Override
    public boolean hasLiquidInputSlot() {
        return true;
    }

}
