package com.unforbidable.tfc.bids.Containers.Slots;

import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileHelper;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWoodPile extends Slot {

    public SlotWoodPile(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return WoodPileHelper.isItemValidWoodPileItem(itemStack);
    }

}
