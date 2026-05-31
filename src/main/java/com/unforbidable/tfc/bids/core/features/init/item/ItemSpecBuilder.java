package com.unforbidable.tfc.bids.core.features.init.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.item.Item;

public class ItemSpecBuilder<T extends Item> {

    private final String name;
    private final Supplier<T> item;

    private Consumer<T> apply;
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
        return new ItemSpec<>(name, item, apply, drink, overlay, mold, meta, harvest, food, smoke);
    }

}
