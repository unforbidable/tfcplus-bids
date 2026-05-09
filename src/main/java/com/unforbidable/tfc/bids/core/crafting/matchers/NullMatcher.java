package com.unforbidable.tfc.bids.core.crafting.matchers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NullMatcher extends ObjectMatcher {

    @Override
    public boolean is(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean is(Item item) {
        return false;
    }

    @Override
    public boolean is(Block block) {
        return false;
    }

    @Override
    public boolean is(String ore) {
        return false;
    }

}
