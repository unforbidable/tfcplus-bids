package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IWoodPileRenderProvider {

    IIcon getWoodPileIcon(ItemStack itemStack, int side, boolean rotated);

    int getWoodPileIconRotation(ItemStack itemStack, int side, boolean rotated);

    float getWoodPileIconScale(ItemStack itemStack);

    boolean isWoodPileLargeItem(ItemStack itemStack);

}
