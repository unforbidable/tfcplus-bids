package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.item.ItemStack;

public interface IFirepitFuelMaterial {

    boolean isFuelValid(ItemStack itemStack);

    float getFuelKindlingQuality(ItemStack itemStack);

    int getFuelBurnTime(ItemStack itemStack);

    int getFuelMaxTemp(ItemStack itemStack);

    int getFuelTasteProfile(ItemStack itemStack);

}
