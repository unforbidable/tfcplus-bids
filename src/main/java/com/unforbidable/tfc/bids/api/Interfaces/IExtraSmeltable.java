package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.item.ItemStack;

public interface IExtraSmeltable {

    float getPurity(ItemStack is);

    boolean isNativeOre(ItemStack is);

}
