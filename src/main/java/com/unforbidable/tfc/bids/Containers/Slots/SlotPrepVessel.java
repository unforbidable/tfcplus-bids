package com.unforbidable.tfc.bids.Containers.Slots;

import com.unforbidable.tfc.bids.api.Crafting.PrepManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotPrepVessel extends TrackedSlot {

    public SlotPrepVessel(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return super.isItemValid(is) && PrepManager.isValidPrepVessel(is);
    }

}
