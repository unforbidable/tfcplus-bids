package com.unforbidable.tfc.bids.Containers.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotPrepOutput extends TrackedSlot {

    public SlotPrepOutput(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return false;
    }

}
