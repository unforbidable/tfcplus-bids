package com.unforbidable.tfc.bids.Core.Seasoning;

import java.util.List;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodHardness;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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

    public static float getWoodSeasoningDurationMultiplier(int damage) {
        final EnumWoodHardness hardness = EnumWoodHardness.fromDamage(damage);
        switch (hardness) {
            case SOFT:
                return 0.7f;

            case MODERATE:
                return 0.9f;

            case HARD:
                return 1f;

            // case INVALID:
            default:
                // Logs that aren't technically wood
                // shouldn't be seasoned
                // but we might still get here if someone tries to
                return 1f;
        }
    }

}
