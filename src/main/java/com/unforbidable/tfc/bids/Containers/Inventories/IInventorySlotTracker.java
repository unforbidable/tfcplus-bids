package com.unforbidable.tfc.bids.Containers.Inventories;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface IInventorySlotTracker {

    void onSlotChanged(IInventory inventory, Slot slot);

    void onPickupFromSlot(IInventory inventory, Slot slot, EntityPlayer player, ItemStack itemStack);

}
