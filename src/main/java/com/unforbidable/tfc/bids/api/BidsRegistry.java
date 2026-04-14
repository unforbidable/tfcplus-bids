package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Interfaces.ISurfaceItemPlacer;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class BidsRegistry {

    public static final ItemRegistry<WetnessInfo> ITEM_WETNESS = new ItemRegistry<>();
    public static final ListRegistry<ISurfaceItemPlacer> SURFACE_PLACERS = new ListRegistry<>();

    public static class ItemRegistry<T> {

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

    public static class ListRegistry<V> implements Iterable<V> {

        private final List<V> values = new ArrayList<>();

        public void register(V value) {
            values.add(value);
        }

        @Override
        public Iterator<V> iterator() {
            return values.iterator();
        }
    }

}
