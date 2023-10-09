package com.unforbidable.tfc.bids.api;

import com.dunk.tfc.api.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BidsFood {

    public static final String TAG_BOILED = "Boiled";
    public static final String TAG_BOILED_LIQUID = "BoiledLiquid";
    public static final String TAG_STEAMED = "Steamed";

    private static ProcTagAccessor getProcTag(ItemStack is) {
        return new ProcTagAccessor(is);
    }

    public static void setBoiled(ItemStack is, boolean value) {
        getProcTag(is).setBoolean(TAG_BOILED, value);
    }

    public static boolean isBoiled(ItemStack is) {
        return getProcTag(is).getBoolean(TAG_BOILED);
    }

    public static void setBoiledLiquid(ItemStack is, String value) {
        getProcTag(is).setString(TAG_BOILED_LIQUID, value);
    }

    public static String getBoiledLiquid(ItemStack is) {
        return getProcTag(is).getString(TAG_BOILED_LIQUID);
    }

    public static void setSteamed(ItemStack is, boolean value) {
        getProcTag(is).setBoolean(TAG_STEAMED, value);
    }

    public static boolean isSteamed(ItemStack is) {
        return getProcTag(is).getBoolean(TAG_STEAMED);
    }

    public static boolean areEqual(ItemStack is1, ItemStack is2) {
        return Food.areEqual(is1, is2) &&
            isBoiled(is1) == isBoiled(is2) &&
            (getBoiledLiquid(is1) == null && getBoiledLiquid(is1) == null || getBoiledLiquid(is1).compareTo(getBoiledLiquid(is2)) == 0) &&
            isSteamed(is1) == isSteamed(is2);
    }

    private static class ProcTagAccessor {
        private final ItemStack itemStack;

        private static final String TAG_PROC = "Processing Tag";

        public ProcTagAccessor(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        private NBTTagCompound getProcTag(ItemStack itemStack) {
            if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(TAG_PROC)) {
                return itemStack.getTagCompound().getCompoundTag(TAG_PROC);
            } else {
                return null;
            }
        }

        private NBTTagCompound getProcTagForUpdate(ItemStack itemStack) {
            if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(TAG_PROC)) {
                return itemStack.getTagCompound().getCompoundTag(TAG_PROC);
            } else {
                NBTTagCompound tag = new NBTTagCompound();

                itemStack.setTagCompound(new NBTTagCompound());
                itemStack.getTagCompound().setTag(TAG_PROC, tag);

                return tag;
            }
        }

        public void setBoolean(String key, boolean value) {
            NBTTagCompound tag = getProcTagForUpdate(itemStack);
            tag.setBoolean(key, value);
        }

        public boolean getBoolean(String key) {
            NBTTagCompound tag = getProcTag(itemStack);
            return tag != null && tag.getBoolean(key);
        }

        public void setString(String key, String value) {
            NBTTagCompound tag = getProcTagForUpdate(itemStack);
            tag.setString(key, value);
        }

        public String getString(String key) {
            NBTTagCompound tag = getProcTag(itemStack);
            return tag != null && tag.hasKey(key) ? tag.getString(key) : null;
        }
    }

}
