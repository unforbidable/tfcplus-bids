package com.unforbidable.tfc.bids.features.device.woodpile.main;

import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IFirepitFuelMaterial;
import net.minecraft.item.ItemStack;

public class WoodpileBurningItem {

    private final int index;
    private final ItemStack itemStack;
    private final IFirepitFuelMaterial fuel;

    public WoodpileBurningItem(int index, ItemStack itemStack, IFirepitFuelMaterial fuel) {
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
