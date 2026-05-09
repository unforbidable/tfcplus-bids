package com.unforbidable.tfc.bids.common.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface ISlotTracker {

    void onSlotChanged(Slot slot);

    void onPickupFromSlot(Slot slot, EntityPlayer player, ItemStack itemStack);

}
