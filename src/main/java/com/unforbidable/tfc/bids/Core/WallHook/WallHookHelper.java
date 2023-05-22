package com.unforbidable.tfc.bids.Core.WallHook;

import com.dunk.tfc.Items.*;
import com.dunk.tfc.Items.Tools.*;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Items.ItemBucketRopeEmpty;
import com.unforbidable.tfc.bids.api.Enums.EnumWallHookPos;
import com.unforbidable.tfc.bids.api.Interfaces.IHangable;
import net.minecraft.item.*;
import net.minecraft.item.ItemShears;

public class WallHookHelper {

    public static boolean canPlaceItemStackOnWallHook(ItemStack is) {
        if (is.getItem() instanceof IHangable) {
            return ((IHangable)is.getItem()).canPlaceOnWallHook(is);
        } else {
            return is.getItem() instanceof ItemTool ||
                is.getItem() instanceof ItemHoe ||
                is.getItem() instanceof ItemProPick ||
                is.getItem() instanceof ItemBow ||
                is.getItem() instanceof ItemSword ||
                is.getItem() instanceof ItemShears ||
                is.getItem() instanceof ItemSpindle ||
                is.getItem() instanceof ItemTrowel ||
                is.getItem() instanceof ItemLeatherBag && ((ISize)is.getItem()).getWeight(is) != EnumWeight.HEAVY ||
                is.getItem() instanceof ItemQuiver ||
                is.getItem() instanceof ItemWaterskin ||
                is.getItem() instanceof ItemFirestarter ||
                is.getItem() instanceof ItemFlintAndSteel ||
                is.getItem() instanceof ItemCustomLeash ||
                is.getItem() == TFCItems.woodenBucketEmpty ||
                is.getItem() instanceof ItemBucketRopeEmpty ||
                is.getItem() instanceof ItemClothing;
        }
    }

    public static EnumWallHookPos getItemStackWallHookPosition(ItemStack is) {
        if (is.getItem() instanceof IHangable) {
            return ((IHangable)is.getItem()).getWallHookPosition(is);
        } else {
            if (is.getItem() instanceof ItemLeatherBag ||
                is.getItem() instanceof ItemFirestarter ||
                is.getItem() instanceof ItemQuiver ||
                is.getItem() == TFCItems.woodenBucketEmpty ||
                is.getItem() instanceof ItemBucketRopeEmpty ||
                is.getItem() instanceof ItemClothing) {
                return EnumWallHookPos.LOW;
            } else if (is.getItem() instanceof ItemBow ||
                is.getItem() instanceof ItemCustomLeash ||
                is.getItem() instanceof ItemWaterskin) {
                return EnumWallHookPos.MID;
            } else {
                return EnumWallHookPos.HIGH;
            }
        }
    }
}
