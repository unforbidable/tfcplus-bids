package com.unforbidable.tfc.bids.features.device.cookingprep.container.slot;

import com.unforbidable.tfc.bids.features.device.cookingprep.main.CookingPrepHelper;
import com.unforbidable.tfc.bids.common.container.slot.TrackedSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotPrepVessel extends TrackedSlot {

    public SlotPrepVessel(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return super.isItemValid(is) && CookingPrepHelper.isValidPrepVessel(is);
    }

}
