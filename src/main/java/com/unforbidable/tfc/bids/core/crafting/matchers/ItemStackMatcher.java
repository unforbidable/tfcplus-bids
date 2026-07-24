package com.unforbidable.tfc.bids.core.crafting.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackMatcher extends ObjectMatcher {

    private final ItemStack itemStack;

    public ItemStackMatcher(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean is(ItemStack itemStack) {
        return OreDictionary.itemMatches(this.itemStack, itemStack, false);
    }

    @Override
    public boolean is(Item item) {
        return itemStack.getItem() == item;
    }

    @Override
    public boolean is(Block block) {
        return itemStack.getItem() == Item.getItemFromBlock(block);
    }

    @Override
    public boolean is(String ore) {
        return OreDictionaryHelper.itemStackIsOre(itemStack, ore);
    }

}
