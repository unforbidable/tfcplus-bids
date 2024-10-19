package com.unforbidable.tfc.bids.Core.Stone;

import net.minecraft.item.ItemStack;

public class StoneBlockProvider {

    private final StoneIndex index;
    private final StoneScheme scheme;

    public StoneBlockProvider(StoneIndex index, StoneScheme scheme) {
        this.index = index;
        this.scheme = scheme;
    }

    public boolean hasBlockStack(EnumStoneBlockType type) {
        return scheme.hasStoneBlockStack(index, type);
    }

    public ItemStack getBlockStack(EnumStoneBlockType type) {
        return scheme.getStoneBlockStack(index, type, 1);
    }

    public ItemStack getBlockStack(EnumStoneBlockType type, int stackSize) {
        return scheme.getStoneBlockStack(index, type, stackSize);
    }

}
