package com.unforbidable.tfc.bids.Containers.Inventories;

import com.unforbidable.tfc.bids.Containers.Slots.ISlotTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class InventoryCraftingTracked extends InventoryCrafting implements ISlotTracker {

    private final Container container;

    public InventoryCraftingTracked(Container container, int width, int height) {
        super(container, width, height);

        this.container = container;
    }

    @Override
    public void onSlotChanged(Slot slot) {
        if (container instanceof IInventorySlotTracker) {
            ((IInventorySlotTracker) container).onSlotChanged(this, slot);
        }
    }

    @Override
    public void onPickupFromSlot(Slot slot, EntityPlayer player, ItemStack itemStack) {
        if (container instanceof IInventorySlotTracker) {
            ((IInventorySlotTracker) container).onPickupFromSlot(this, slot, player, itemStack);
        }
    }

}
