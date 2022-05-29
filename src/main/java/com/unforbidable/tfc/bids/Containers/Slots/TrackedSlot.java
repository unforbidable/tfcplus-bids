package com.unforbidable.tfc.bids.Containers.Slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

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

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {
        super.onPickupFromSlot(player, itemStack);

        if (inventory instanceof ISlotTracker) {
            ((ISlotTracker) inventory).onPickupFromSlot(this, player, itemStack);
        }
    }

}
