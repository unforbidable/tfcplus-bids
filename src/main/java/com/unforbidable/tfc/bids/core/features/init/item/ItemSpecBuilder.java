package com.unforbidable.tfc.bids.core.features.init.item;

import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemSpecBuilder<T extends Item> {

    private final String name;
    private final Supplier<T> item;

    private Consumer<T> apply;
    private ContainerSpec container;
    private FluidSpec fluid;
    private DrinkSpec drink;
    private OverlaySpec overlay;
    private MoldSpec mold;
    private MetaSpec meta;
    private final List<ItemHarvestSpec> harvest = new ArrayList<>();
    private FoodSpec food;
    private SmokeSpec smoke;

    public ItemSpecBuilder(String name, Supplier<T> item) {
        this.name = name;
        this.item = item;
    }

    public ItemSpecBuilder<T> apply(Consumer<T> apply) {
        if (this.apply == null) {
            this.apply = apply;
        } else {
            this.apply = this.apply.andThen(apply);
        }

        return this;
    }

    /**
     * Specifies the item that is the empty container.
     * @param item Empty container item
     * @return This <code>ItemSpecBuilder</code> instance.
     */
    public ItemSpecBuilder<T> container(Item item) {
        return container(() -> item, 0);
    }

    /**
     * <p>Specifies the item that is the empty container, and the damage.</p>
     * <p>Pottery empty containers typically specify damage value of 1, 0 being the unfired item.
     * For containers with sub items, the damage value is added to the index of the sub item.</p>
     * <p>Containers with sub items currently do not support partial fluids.</p>
     * @param item Empty container item
     * @param emptyItemDamage Empty container item damage
     * @return This <code>ItemSpecBuilder</code> instance.
     */
    public ItemSpecBuilder<T> container(Item item, int emptyItemDamage) {
        container = new ContainerSpec(() -> item, emptyItemDamage);

        return this;
    }

    /**
     * <p>Specifies the item that is the empty container, and the damage.</p>
     * <p>Unlike <code>container(Item item, int emptyItemDamage)</code>, this method accepts</p>
     * <code>Supplier&lt;Item&gt;</code>, which is invoked only once the <code>Item</code> instance is actually created.
     * This is necessary in case the container item is initialized in the same feature where it is referenced in.
     * @param item Empty container item
     * @param emptyItemDamage Empty container item damage
     * @return This <code>ItemSpecBuilder</code> instance.
     */
    public ItemSpecBuilder<T> container(Supplier<Item> item, int emptyItemDamage) {
        container = new ContainerSpec(item, emptyItemDamage);

        return this;
    }

    public ItemSpecBuilder<T> fluid(int volume, Fluid fluid, boolean partial) {
        this.fluid = new FluidSpec(volume, fluid, partial);

        return this;
    }

    public ItemSpecBuilder<T> fluid(int volume, Fluid fluid) {
        return fluid(volume, fluid, false);
    }

    public ItemSpecBuilder<T> drink(int volume, boolean pottery) {
        drink = new DrinkSpec(volume, pottery);

        return this;
    }

    public ItemSpecBuilder<T> overlays(int ...partialOverlays) {
        overlay = new OverlaySpec(partialOverlays);

        return this;
    }

    public ItemSpecBuilder<T> mold(int counter, String ...metals) {
        mold = new MoldSpec(counter, metals);

        return this;
    }

    public ItemSpecBuilder<T> meta(String ...names) {
        meta = new MetaSpec(names);

        return this;
    }

    public ItemSpecBuilder<T> harvest(String toolClass, int level) {
        harvest.add(new ItemHarvestSpec(toolClass, level));

        return this;
    }

    public ItemSpecBuilder<T> food(float decayRate) {
        return food(decayRate, 0f);
    }

    public ItemSpecBuilder<T> food(float decayRate, float waterPercentage) {
        return food(decayRate, waterPercentage, true, true);
    }

    public ItemSpecBuilder<T> food(float decayRate, float waterPercentage, boolean edible, boolean canBeUsedRaw) {
        return food(decayRate, waterPercentage, edible, canBeUsedRaw, false, false);
    }

    public ItemSpecBuilder<T> food(float decayRate, boolean edible, boolean canBeUsedRaw) {
        return food(decayRate, 0f, edible, canBeUsedRaw, false, false);
    }

    public ItemSpecBuilder<T> food(float decayRate, boolean edible, boolean canBeUsedRaw, boolean poisonOnRaw, boolean guaranteedPoisonOnRaw) {
        return food(decayRate, 0f, edible, canBeUsedRaw, poisonOnRaw, guaranteedPoisonOnRaw);
    }

    public ItemSpecBuilder<T> food(float decayRate, float waterPercentage, boolean edible, boolean canBeUsedRaw, boolean poisonOnRaw, boolean guaranteedPoisonOnRaw) {
        food = new FoodSpec(decayRate, waterPercentage, edible, canBeUsedRaw, poisonOnRaw, guaranteedPoisonOnRaw);

        return this;
    }

    public ItemSpecBuilder<T> smoke() {
        return smoke(0.5f);
    }

    public ItemSpecBuilder<T> smoke(float smokeAbsorbMultiplier) {
        smoke = new SmokeSpec(smokeAbsorbMultiplier);

        return this;
    }

    public ItemSpec<T> build() {
        return new ItemSpec<>(name, item, apply, container, fluid, drink, overlay, mold, meta, harvest, food, smoke);
    }

}
