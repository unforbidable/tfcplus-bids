package com.unforbidable.tfc.bids.features.device.wallhook.main;

import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.Items.ItemCustomLeash;
import com.dunk.tfc.Items.ItemLeatherBag;
import com.dunk.tfc.Items.ItemQuiver;
import com.dunk.tfc.Items.ItemWaterskin;
import com.dunk.tfc.Items.Tools.ItemFirestarter;
import com.dunk.tfc.Items.Tools.ItemProPick;
import com.dunk.tfc.Items.Tools.ItemSpindle;
import com.dunk.tfc.Items.Tools.ItemTrowel;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.wallhook.Hangable;
import com.unforbidable.tfc.bids.api.features.wallhook.WallHookPos;
import com.unforbidable.tfc.bids.common.item.ItemCommonTool;
import com.unforbidable.tfc.bids.features.resource.well.item.ItemBucketRopeEmpty;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class WallHookHelper {

    // TODO use event in place of an interface

    public static boolean canPlaceItemStackOnWallHook(ItemStack is) {
        if (is.getItem() instanceof Hangable) {
            return ((Hangable)is.getItem()).canPlaceOnWallHook(is);
        } else {
            return is.getItem() instanceof ItemTool ||
                is.getItem() instanceof ItemCommonTool ||
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

    public static WallHookPos getItemStackWallHookPosition(ItemStack is) {
        if (is.getItem() instanceof Hangable) {
            return ((Hangable)is.getItem()).getWallHookPosition(is);
        } else {
            if (is.getItem() instanceof ItemLeatherBag ||
                is.getItem() instanceof ItemFirestarter ||
                is.getItem() instanceof ItemQuiver ||
                is.getItem() == TFCItems.woodenBucketEmpty ||
                is.getItem() instanceof ItemBucketRopeEmpty ||
                is.getItem() instanceof ItemClothing) {
                return WallHookPos.LOW;
            } else if (is.getItem() instanceof ItemBow ||
                is.getItem() instanceof ItemCustomLeash ||
                is.getItem() instanceof ItemWaterskin) {
                return WallHookPos.MID;
            } else {
                return WallHookPos.HIGH;
            }
        }
    }
}
