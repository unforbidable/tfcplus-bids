package com.unforbidable.tfc.bids.api.features.quarry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface PlugAndFeather {

    int getPlugAndFeatherQuarryEquipmentTier(ItemStack itemStack);

    float getPlugAndFeatherDropRate(ItemStack itemStack);

    Block getPlugAndFeatherRenderBlock(ItemStack itemStack);

    int getPlugAndFeatherRenderBlockMetadata(ItemStack itemStack);

}
