package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.api.Enums.EnumWallHookPos;
import net.minecraft.item.ItemStack;

public interface IHangable {

    EnumWallHookPos getWallHookPosition(ItemStack itemStack);

    boolean canPlaceOnWallHook(ItemStack itemStack);

}
