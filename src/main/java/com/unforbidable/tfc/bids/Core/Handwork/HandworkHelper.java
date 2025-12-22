package com.unforbidable.tfc.bids.Core.Handwork;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HandworkHelper {

    public static HandworkProgress loadHandworkProgress(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().hasKey("handwork")) {
            return HandworkProgress.loadItemStackFromNBT(is.getTagCompound().getCompoundTag("handwork"));
        } else {
            return null;
        }
    }

    public static void writeHandworkProgress(ItemStack is, HandworkProgress progress) {
        if (!is.hasTagCompound()) {
            is.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = new NBTTagCompound();
        progress.writeToNBT(tag);
        is.getTagCompound().setTag("handwork", tag);
    }

    public static void clearHandworkProgress(ItemStack is) {
        if (is.hasTagCompound()) {
            is.getTagCompound().removeTag("handwork");
            if (is.getTagCompound().hasNoTags()) {
                is.setTagCompound(null);
            }
        }
    }

}
