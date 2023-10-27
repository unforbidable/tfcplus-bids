package com.unforbidable.tfc.bids.Containers.Slots;

import com.dunk.tfc.api.Interfaces.IFood;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class SlotPrepIngredient extends TrackedSlot {

    public SlotPrepIngredient(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return super.isItemValid(is) &&
            is.getItem() instanceof IFood &&
            !FluidContainerRegistry.isFilledContainer(is);
    }

}
