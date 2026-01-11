package com.unforbidable.tfc.bids.Containers.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotOutputOnlyTracked extends TrackedSlot {

    public SlotOutputOnlyTracked(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

}
