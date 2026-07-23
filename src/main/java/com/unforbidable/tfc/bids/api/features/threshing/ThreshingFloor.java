package com.unforbidable.tfc.bids.api.features.threshing;

import net.minecraft.item.ItemStack;

public interface ThreshingFloor {

    boolean takeGrain(ItemStack result);
    boolean takeStraw(ItemStack result);

}
