package com.unforbidable.tfc.bids.features.device.screwpress.container.slot;

import com.unforbidable.tfc.bids.features.device.screwpress.main.ScrewPressHelper;
import com.unforbidable.tfc.bids.common.container.slot.TrackedSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotScrewPressInput extends TrackedSlot {

    public SlotScrewPressInput(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return ScrewPressHelper.isValidScrewPressInputItem(is);
    }

}
