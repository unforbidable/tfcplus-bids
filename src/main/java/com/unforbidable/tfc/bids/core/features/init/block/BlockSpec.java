package com.unforbidable.tfc.bids.core.features.init.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockSpec<T extends Block> {

    public final String name;
    private final Supplier<T> block;
    public final Class<? extends ItemBlock> itemType;
    private final Consumer<T> apply;
    public final BlockFireInfoSpec fireInfo;
    public final BlockHarvestSpec harvest;

    public BlockSpec(String name, Supplier<T> block, Class<? extends ItemBlock> itemType, Consumer<T> apply,
                     BlockFireInfoSpec fireInfo, BlockHarvestSpec harvest) {
        this.name = name;
        this.itemType = itemType;
        this.block = block;
        this.apply = apply;
        this.fireInfo = fireInfo;
        this.harvest = harvest;
    }

    public T getInstance() {
        T instance = block.get();
        instance.setBlockName(name);

        if (harvest != null) {
            instance.setHarvestLevel(harvest.toolClass, harvest.level);
        }

        if (apply != null) {
            apply.accept(instance);
        }

        return instance;
    }

}
