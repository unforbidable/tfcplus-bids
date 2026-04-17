package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Crafting.CarvingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.ChurningRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.*;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.function.Predicate;

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

    public static final RecipeRegistry<CarvingRecipe> CARVING_RECIPES = new RecipeRegistry<>();
    public static final SimpleRecipeRegistry<ChurningRecipe, FluidStack> CHURNING_RECIPES = new SimpleRecipeRegistry<>();

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

    public static class RecipeRegistry<T> implements Iterable<T> {

        private final List<T> recipes = new ArrayList<>();

        public void register(T recipe) {
            recipes.add(recipe);
        }

        public T findMatchingRecipe(Predicate<T> predicate) {
            return recipes.stream().filter(predicate).findFirst().orElse(null);
        }

        @Override
        public Iterator<T> iterator() {
            return recipes.iterator();
        }
    }

    public static class SimpleRecipeRegistry<R extends ISimpleRecipeMatcher<I>, I> extends RecipeRegistry<R> {

        public R findMatchingRecipe(I ingredient) {
            return super.findMatchingRecipe(r -> r.matches(ingredient));
        }

    }

    public static class ItemRegistry<T> extends MapRegistry<Item, T> {

        public void register(Block block, T value) {
            super.register(Item.getItemFromBlock(block), value);
        }

        @Override
        protected String getName(Item key) {
            return key.getUnlocalizedName();
        }

    }

    public static class BlockRegistry<T> extends MapRegistry<Block, T> {

        @Override
        protected String getName(Block key) {
            return key.getUnlocalizedName();
        }

    }

    public static class FluidRegistry<T> extends MapRegistry<Fluid, T> {

        @Override
        protected String getName(Fluid key) {
            return key.getUnlocalizedName();
        }

    }

    protected static class MapRegistry<K, V> extends MapRegistryBase<K, V, Entry<K, V>> {

        public void register(K key, V value) {
            super.register(key, new Entry<>(key, value));
        }

    }

    protected static class MapRegistryBase<K, V, E extends Entry<K, V>> implements Iterable<E> {

        private final Map<String, E> values = new HashMap<>();

        protected void register(K key, E entry) {
            register(getName(key), entry);
        }

        protected void register(String name, E entry) {
            values.put(name, entry);
        }

        public boolean has(K key) {
            return has(getName(key));
        }

        protected boolean has(String name) {
            return values.containsKey(name);
        }

        public V get(K key) {
            return get(getName(key));
        }

        protected V get(String name) {
            E entry = values.get(name);
            return entry != null ? entry.value : null;
        }

        protected String getName(K key) {
            return key.toString();
        }

        @Override
        public Iterator<E> iterator() {
            return values.values().iterator();
        }

    }

    public static class Entry<K, V> {
        public final K key;
        public final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
