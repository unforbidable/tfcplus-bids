package com.unforbidable.tfc.bids.Core.Handwork;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.Interfaces.IHandworkToolMaterial;
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

    public static int getColorFromMaterial(ItemStack is, int pass) {
        if (is.getItem() instanceof IHandworkToolMaterial) {
            return ((IHandworkToolMaterial) is.getItem()).getColorFromMaterial(is, pass);
        }

        if (is.getItem() == TFCItems.linenString) {
            return 0xccebcc;
        } else if (is.getItem() == TFCItems.cottonYarn) {
            return 0xe2e9df;
        } else if (is.getItem() == TFCItems.woolYarn) {
            return 0xe3e3d2;
        }

        return is.getItem().getColorFromItemStack(is, pass);
    }

}
