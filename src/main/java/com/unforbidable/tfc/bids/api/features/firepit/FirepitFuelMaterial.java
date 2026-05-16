package com.unforbidable.tfc.bids.api.features.firepit;

import net.minecraft.item.ItemStack;

public interface FirepitFuelMaterial {

    boolean isFuelValid(ItemStack itemStack);

    float getFuelKindlingQuality(ItemStack itemStack);

    int getFuelBurnTime(ItemStack itemStack);

    int getFuelMaxTemp(ItemStack itemStack);

    int getFuelTasteProfile(ItemStack itemStack);

}
