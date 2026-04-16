package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public interface IDryingItemRenderInfo {

    IIcon getRenderIcon(ItemStack itemStack);
    int getRenderColor(ItemStack itemStack);
    AxisAlignedBB getRenderBounds(ItemStack itemStack);

}
