package com.unforbidable.tfc.bids.features.device.woodpile.main.seasoning;

import com.unforbidable.tfc.bids.core.schemes.wood.EnumWoodItemType;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
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

    public static int getWoodSeasoningDuration(WoodIndex wood, EnumWoodItemType type) {
        // Wood types without bark season very fast - i.e. bamboo
        // Hardwoods season slowest
        int baseDuration = !wood.hasBark ? 12 : (wood.hardwood ? 24 : 18);

        switch (type) {
            case FIREWOOD:
                return baseDuration - 4;

            case PEELED_LOG:
                return baseDuration - 2;

            default:
                return baseDuration;
        }
    }

}
