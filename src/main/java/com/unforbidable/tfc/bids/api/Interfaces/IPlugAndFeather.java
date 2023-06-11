package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.api.Enums.EnumQuarryEquipmentTier;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface IPlugAndFeather {

    EnumQuarryEquipmentTier getPlugAndFeatherQuarryEquipmentTier(ItemStack itemStack);

    float getPlugAndFeatherDropRate(ItemStack itemStack);

    Block getPlugAndFeatherRenderBlock(ItemStack itemStack);

    int getPlugAndFeatherRenderBlockMetadata(ItemStack itemStack);

}
