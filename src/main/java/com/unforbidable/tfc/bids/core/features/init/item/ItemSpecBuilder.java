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

    public ItemSpecBuilder<T> container(String itemName) {
        return container(itemName, 0);
    }

    public ItemSpecBuilder<T> container(String itemName, int emptyItemDamage) {
        container = new ContainerSpec(ItemResolver.of(itemName), emptyItemDamage);

        return this;
    }

    public ItemSpecBuilder<T> container(Item item) {
        return container(item, 0);
    }

    public ItemSpecBuilder<T> container(Item item, int emptyItemDamage) {
        container = new ContainerSpec(ItemResolver.of(item), emptyItemDamage);

        return this;
    }

    public ItemSpecBuilder<T> fluid(int volume, Fluid fluid, boolean partial) {
        this.fluid = new FluidSpec(volume, fluid, partial);

        return this;
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

    public ItemSpec<T> build() {
        return new ItemSpec<>(name, item, apply, container, fluid, drink, overlay, mold, meta, harvest);
    }

}
