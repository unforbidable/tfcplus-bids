package com.unforbidable.tfc.bids.api.features.wallhook;

import net.minecraft.item.ItemStack;

public interface Hangable {

    WallHookPos getWallHookPosition(ItemStack itemStack);

    boolean canPlaceOnWallHook(ItemStack itemStack);

}
