package com.unforbidable.tfc.bids.features.device.woodpile.main;

import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import net.minecraft.item.ItemStack;

public class WoodpileBurningItem {

    private final int index;
    private final ItemStack itemStack;
    private final FirepitFuelMaterial fuel;

    public WoodpileBurningItem(int index, ItemStack itemStack, FirepitFuelMaterial fuel) {
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

    public FirepitFuelMaterial getFuel() {
        return fuel;
    }

}
