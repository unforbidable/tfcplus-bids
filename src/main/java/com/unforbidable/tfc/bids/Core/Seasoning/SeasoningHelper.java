package com.unforbidable.tfc.bids.Core.Seasoning;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class SeasoningHelper {

    static final int SEASONING_RATIO_PRECISION = 1000;

    public static void setItemSeasoningTag(ItemStack itemStack, float seasoning) {
        NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        tag.setInteger("seasoning", Math.round(seasoning * SEASONING_RATIO_PRECISION));
        itemStack.setTagCompound(tag);
    }

    public static float getItemSeasoningTag(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            return (float) itemStack.getTagCompound().getInteger("seasoning") / (float) SEASONING_RATIO_PRECISION;
        }

        return 0;
    }

    public static boolean hasItemSeasoningTag(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("seasoning");
    }

    public static void removeItemSeasoningTag(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            itemStack.getTagCompound().removeTag("seasoning");
        }
    }

    public static void addSeasoningInformation(ItemStack itemStack, List<String> list) {
        if (itemStack.hasTagCompound()) {
            int seasoning = itemStack.getTagCompound().getInteger("seasoning");
            list.add(ChatFormatting.GOLD + StatCollector.translateToLocal("gui.Seasoning")
                    + ": " + ((float) seasoning * 100 / (float) SEASONING_RATIO_PRECISION) + "% (" + seasoning + ")");
        }
    }

}
