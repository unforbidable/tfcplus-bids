package com.unforbidable.tfc.bids.api.util.metal;

import net.minecraft.item.ItemStack;

public interface MoreSmeltable {

    float getPurity(ItemStack is);

    boolean isNativeOre(ItemStack is);

}
