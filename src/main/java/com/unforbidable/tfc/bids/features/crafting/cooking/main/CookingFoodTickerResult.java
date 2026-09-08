package com.unforbidable.tfc.bids.features.crafting.cooking.main;

import net.minecraft.item.ItemStack;

public class CookingFoodTickerResult {

    public final ItemStack result;
    public final boolean updated;

    public CookingFoodTickerResult(ItemStack result, boolean updated) {
        this.result = result;
        this.updated = updated;
    }

}
