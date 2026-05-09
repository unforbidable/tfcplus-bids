package com.unforbidable.tfc.bids.api._obsolete.Registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ItemRegistry<T> extends MapRegistry<Item, T> {

    public void register(Block block, T value) {
        super.register(Item.getItemFromBlock(block), value);
    }

    @Override
    protected String getName(Item key) {
        return key.getUnlocalizedName();
    }

}
