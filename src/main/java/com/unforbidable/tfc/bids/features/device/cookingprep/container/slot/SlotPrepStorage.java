package com.unforbidable.tfc.bids.features.device.cookingprep.container.slot;

import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.common.container.slot.TrackedSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotPrepStorage extends TrackedSlot {

    public SlotPrepStorage(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return is.getItem() instanceof ISize && ((ISize)is.getItem()).getSize(is).ordinal() <= EnumSize.SMALL.ordinal();
    }

}
