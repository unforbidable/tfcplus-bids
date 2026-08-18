package com.unforbidable.tfc.bids.api.features.surfaceitem;

import net.minecraft.item.ItemStack;

public interface ItemSurfaceIconAccessor {

    void setSurfaceIconName(String surfaceIconName);

    String getSurfaceIconName(ItemStack itemStack);

}
