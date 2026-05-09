package com.unforbidable.tfc.bids.api._obsolete.Interfaces;

import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumWallHookPos;
import net.minecraft.item.ItemStack;

public interface IHangable {

    EnumWallHookPos getWallHookPosition(ItemStack itemStack);

    boolean canPlaceOnWallHook(ItemStack itemStack);

}
