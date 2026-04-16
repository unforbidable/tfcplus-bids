package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Interfaces.IDryingItemRenderInfo;
import com.unforbidable.tfc.bids.api.Interfaces.ISurfaceItemPlacer;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class BidsRegistry {

    public static final ItemRegistry<WetnessInfo> ITEM_WETNESS = new ItemRegistry<>();
    public static final ItemRegistry<IDryingItemRenderInfo> ITEM_DRYING_RENDER_INFO = new ItemRegistry<>();
    public static final ListRegistry<ISurfaceItemPlacer> SURFACE_PLACERS = new ListRegistry<>();

    public static class ItemRegistry<T> implements Iterable<ItemRegistry.Entry<T>> {

        public static final int WILD = 32767;

        private final Map<String, Entry<T>> values = new HashMap<>();

        public void register(Item item, T value) {
            register(new ItemStack(item, 1, WILD), value);
        }

        public void register(ItemStack itemStack, T value) {
            values.put(getKeyForItemStack(itemStack), new Entry<>(itemStack.getItem(), itemStack.getItemDamage(), value));
        }

        public T get(Item item) {
            Entry<T> entryWildcard = values.get(getKeyForItemWithWildcard(item));
            if (entryWildcard != null) {
                return entryWildcard.value;
            }

            return null;
        }

        public T get(ItemStack itemStack) {
            Entry<T> entry = values.get(getKeyForItemStack(itemStack));
            if (entry != null) {
                return entry.value;
            } else {
                Entry<T> entryWildcard = values.get(getKeyForItemWithWildcard(itemStack.getItem()));
                if (entryWildcard != null) {
                    return entryWildcard.value;
                }
            }

            return null;
        }

        public boolean has(Item item) {
            return values.get(getKeyForItemWithWildcard(item)) != null;
        }

        public boolean has(ItemStack itemStack) {
            return values.get(getKeyForItemStack(itemStack)) != null || values.get(getKeyForItemWithWildcard(itemStack.getItem())) != null;
        }

        private static String getKeyForItemStack(ItemStack itemStack) {
            return itemStack.getItem().getUnlocalizedName() + "@" + itemStack.getItemDamage();
        }

        private static String getKeyForItemWithWildcard(Item item) {
            return item.getUnlocalizedName() + "@" + WILD;
        }

        @Override
        public Iterator<Entry<T>> iterator() {
            return values.values().iterator();
        }

        public static class Entry<T> {
            public final Item item;
            public final int damage;
            public final T value;

            public Entry(Item item, int damage, T value) {
                this.item = item;
                this.damage = damage;
                this.value = value;
            }
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
