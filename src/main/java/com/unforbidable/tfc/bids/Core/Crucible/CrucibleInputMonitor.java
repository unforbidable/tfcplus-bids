package com.unforbidable.tfc.bids.Core.Crucible;

import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;

import net.minecraft.item.ItemStack;

public class CrucibleInputMonitor {

    int count;
    int volume;
    float heatCapacity;
    float purity;
    boolean valid;

    boolean dirty = true;

    final TileEntityCrucible te;

    public CrucibleInputMonitor(TileEntityCrucible te) {
        this.te = te;
    }

    public void makeDirty() {
        dirty = true;
    }

    public int getItemCount() {
        if (dirty)
            update();

        return count;
    }

    public int getVolume() {
        if (dirty)
            update();

        return volume;
    }

    public float getHeatCapacity() {
        if (dirty)
            update();

        return heatCapacity;
    }

    public float getPurity() {
        if (dirty)
            update();

        return purity;
    }

    public boolean isValid() {
        if (dirty)
            update();

        return valid;
    }

    private void update() {
        valid = true;
        count = 0;
        volume = 0;
        heatCapacity = 0;
        float totalPurity = 0;
        for (int i = 0; i < te.getInputSlotCount(); i++) {
            ItemStack is = te.getStackInSlot(i);
            if (is != null) {
                if (te.isItemStackValidInput(is)) {
                    count += is.stackSize;
                    volume += CrucibleHelper.getMetalReturnAmount(is) * is.stackSize;
                    heatCapacity += CrucibleHelper.getHeatCapacity(is) * CrucibleHelper.getMetalReturnAmount(is)
                            * is.stackSize;
                    totalPurity += CrucibleHelper.getPurity(is) * CrucibleHelper.getMetalReturnAmount(is)
                            * is.stackSize;
                } else {
                    valid = false;
                }
            }
        }
        purity = volume > 0 ? totalPurity / volume : 0;

        dirty = false;
    }

}
