package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BidsRegistry {

    public static final Registry<WetnessInfo> WETNESS = new Registry<>();

    public static class Registry<T> {

        public static final int WILD = 32767;

        private final Map<String, T> values = new HashMap<>();

        public void register(Item item, T value) {
            register(new ItemStack(item, 1, WILD), value);
        }

        public void register(ItemStack itemStack, T value) {
            values.put(getItemKey(itemStack), value);
        }

        public T get(ItemStack itemStack) {
            T value = values.get(getItemKey(itemStack));
            return value != null ? value : values.get(getItemKeyWildcard(itemStack));
        }

        private static String getItemKey(ItemStack itemStack) {
            return itemStack.getUnlocalizedName() + "@" + itemStack.getItemDamage();
        }

        private static String getItemKeyWildcard(ItemStack itemStack) {
            return itemStack.getUnlocalizedName() + "@" + WILD;
        }
    }

}
