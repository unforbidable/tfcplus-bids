package com.unforbidable.tfc.bids.Containers.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class TrackedSlot extends Slot {

    public TrackedSlot(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();

        if (inventory instanceof ISlotTracker) {
            ((ISlotTracker) inventory).onSlotChanged(this);
        }
    }

}
