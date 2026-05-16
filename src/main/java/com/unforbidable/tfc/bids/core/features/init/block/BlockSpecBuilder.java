package com.unforbidable.tfc.bids.core.features.init.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockSpecBuilder<T extends Block> {

    private final String name;
    private final Supplier<T> block;
    private final Class<? extends ItemBlock> itemType;

    private Consumer<T> apply;

    private BlockFireInfoSpec fireInfo;
    private BlockHarvestSpec harvestability;
    private MetaSpec meta;

    public BlockSpecBuilder(String name, Supplier<T> block, Class<? extends ItemBlock> itemType) {
        this.name = name;
        this.block = block;
        this.itemType = itemType;
    }

    public BlockSpecBuilder<T> apply(Consumer<T> apply) {
        if (this.apply == null) {
            this.apply = apply;
        } else {
            this.apply = this.apply.andThen(apply);
        }

        return this;
    }

    public BlockSpecBuilder<T> fireInfo(int encouragement, int flammability) {
        this.fireInfo = new BlockFireInfoSpec(encouragement, flammability);

        return this;
    }

    public BlockSpecBuilder<T> harvest(String toolClass, int level) {
        this.harvestability = new BlockHarvestSpec(toolClass, level);

        return this;
    }

    public BlockSpecBuilder<T> meta(String ...stoneSed) {
        this.meta = new MetaSpec(stoneSed);

        return this;
    }

    public BlockSpec<T> build() {
        return new BlockSpec<>(name, block, itemType, apply, fireInfo, harvestability, meta);
    }

}
