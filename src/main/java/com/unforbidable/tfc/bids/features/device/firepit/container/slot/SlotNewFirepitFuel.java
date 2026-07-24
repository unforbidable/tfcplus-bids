package com.unforbidable.tfc.bids.features.device.firepit.container.slot;

import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNewFirepitFuel extends Slot {
    public SlotNewFirepitFuel(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        final FirepitFuelMaterial fuel = FirepitRegistry.fuel.get(itemStack.getItem());
        return fuel != null && fuel.isFuelValid(itemStack);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public void putStack(ItemStack itemStack) {
        if (itemStack != null) {
            itemStack.stackSize = 1;
        }

        super.putStack(itemStack);
    }
}
