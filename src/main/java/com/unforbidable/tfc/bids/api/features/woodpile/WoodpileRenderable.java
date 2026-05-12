package com.unforbidable.tfc.bids.api.features.woodpile;

import net.minecraft.item.ItemStack;

public interface WoodpileRenderable {

    boolean renderAsLargeWoodpileItem(ItemStack itemStack);

    void configureWoodpileRenderer(ItemStack itemStack, boolean rotated, WoodpileRenderConfigurator renderer);

}
