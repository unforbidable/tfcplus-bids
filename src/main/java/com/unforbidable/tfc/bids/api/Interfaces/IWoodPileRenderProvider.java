package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.item.ItemStack;

public interface IWoodPileRenderProvider {

    boolean isWoodPileLargeItem(ItemStack itemStack);

    void onWoodPileRender(ItemStack itemStack, boolean rotated, IWoodPileRenderer renderer);

}
