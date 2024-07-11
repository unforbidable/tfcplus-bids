package com.unforbidable.tfc.bids.Core.WoodPile;

import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;
import net.minecraft.item.ItemStack;

public class WoodPileBurningItem {

    private final int index;
    private final ItemStack itemStack;
    private final IFirepitFuelMaterial fuel;

    public WoodPileBurningItem(int index, ItemStack itemStack, IFirepitFuelMaterial fuel) {
        this.index = index;
        this.itemStack = itemStack;
        this.fuel = fuel;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public IFirepitFuelMaterial getFuel() {
        return fuel;
    }

}
