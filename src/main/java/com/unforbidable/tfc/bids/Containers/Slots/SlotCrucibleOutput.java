package com.unforbidable.tfc.bids.Containers.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotCrucibleOutput extends TrackedSlot {

    public SlotCrucibleOutput(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return inventory.isItemValidForSlot(slotNumber, is);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

}
