package com.unforbidable.tfc.bids.Core.Stone;

import net.minecraft.item.ItemStack;

public class StoneItemProvider {

    private final StoneIndex index;
    private final StoneScheme scheme;

    public StoneItemProvider(StoneIndex index, StoneScheme scheme) {
        this.index = index;
        this.scheme = scheme;
    }

    public boolean hasItem(EnumStoneItemType type) {
        return scheme.hasStoneItemStack(index, type);
    }

    public ItemStack getItem(EnumStoneItemType type) {
        return scheme.getStoneItemStack(index, type, 1);
    }

    public ItemStack getItem(EnumStoneItemType type, int stackSize) {
        return scheme.getStoneItemStack(index, type, stackSize);
    }

}
