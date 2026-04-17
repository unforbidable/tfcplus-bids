package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Interfaces.*;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.*;

public class BidsRegistry {

    public static final ItemRegistry<WetnessInfo> DRYING_ITEM_WETNESS = new ItemRegistry<>();
    public static final ItemRegistry<IDryingItemRenderInfo> DRYING_ITEM_RENDER_INFO = new ItemRegistry<>();
    public static final ItemRegistry<IFirepitFuelMaterial> FIREPIT_FUEL = new ItemRegistry<>();
    public static final ItemRegistry<IWoodPileRenderProvider> WOODPILE_RENDER_PROVIDERS = new ItemRegistry<>();
    public static final ListRegistry<ICrackableBlock> WOODPILE_CRACKABLE_BLOCKS = new ListRegistry<>();
    public static final ListRegistry<ISurfaceItemPlacer> SURFACE_ITEM_PLACERS = new ListRegistry<>();
    public static final BlockRegistry<IQuarriable> QUARRY_BLOCKS = new BlockRegistry<>();
    public static final ListRegistry<ICarving> CARVING_BLOCKS = new ListRegistry<>();
    public static final ListRegistry<IDrinkable> DRINKS = new ListRegistry<>();
    public static final ListRegistry<Class<? extends IKilnChamber>> KILN_CHAMBERS = new ListRegistry<>();
    public static final FluidRegistry<ILampFuelMaterial> LAMP_FUEL = new FluidRegistry<>();

    public static class ItemRegistry<T> implements Iterable<ItemRegistry.Entry<T>> {

        public static final int WILD = 32767;

        private final Map<String, Entry<T>> values = new HashMap<>();

        public void register(Item item, T value) {
            register(new ItemStack(item, 1, WILD), value);
        }

        public void register(Block block, T value) {
            register(Item.getItemFromBlock(block), value);
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

    public static class BlockRegistry<T> implements Iterable<BlockRegistry.Entry<T>> {

        private final Map<String, Entry<T>> values = new HashMap<>();

        public void register(Block block, T value) {
            values.put(getKeyForBlock(block), new Entry<>(block, value));
        }

        public T get(Block block) {
            Entry<T> entry = values.get(getKeyForBlock(block));
            if (entry != null) {
                return entry.value;
            }

            return null;
        }

        public boolean has(Block block) {
            return values.get(getKeyForBlock(block)) != null;
        }

        private static String getKeyForBlock(Block block) {
            return block.getUnlocalizedName();
        }

        @Override
        public Iterator<Entry<T>> iterator() {
            return values.values().iterator();
        }

        public static class Entry<T> {
            public final Block block;
            public final T value;

            public Entry(Block block, T value) {
                this.block = block;
                this.value = value;
            }
        }

    }

    public static class FluidRegistry<T> implements Iterable<FluidRegistry.Entry<T>> {

        private final Map<String, Entry<T>> values = new HashMap<>();

        public void register(Fluid fluid, T value) {
            values.put(getKeyForFluid(fluid), new Entry<>(fluid, value));
        }

        public T get(Fluid fluid) {
            Entry<T> entry = values.get(getKeyForFluid(fluid));
            if (entry != null) {
                return entry.value;
            }

            return null;
        }

        public boolean has(Fluid fluid) {
            return values.get(getKeyForFluid(fluid)) != null;
        }

        private static String getKeyForFluid(Fluid fluid) {
            return fluid.getUnlocalizedName();
        }

        @Override
        public Iterator<Entry<T>> iterator() {
            return values.values().iterator();
        }

        public static class Entry<T> {
            public final Fluid fluid;
            public final T value;

            public Entry(Fluid fluid, T value) {
                this.fluid = fluid;
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
