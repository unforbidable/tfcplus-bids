package com.unforbidable.tfc.bids.Core.Seasoning;

import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

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
        // Since the seasoning progress is seen on the item icon
        // showing it as item info text is redundant
        //
        // if (itemStack.hasTagCompound()) {
        // int seasoning = itemStack.getTagCompound().getInteger("seasoning");
        // list.add(ChatFormatting.GOLD +
        // StatCollector.translateToLocal("gui.Seasoning")
        // + ": " + ((float) seasoning * 100 / (float) SEASONING_RATIO_PRECISION) + "%
        // (" + seasoning + ")");
        // }
    }

    public static int getWoodSeasoningDuration(int damage) {
        WoodIndex wood = WoodScheme.DEFAULT.findWood(damage);
        return wood.hardwood ? 24 : 18;
    }

}
