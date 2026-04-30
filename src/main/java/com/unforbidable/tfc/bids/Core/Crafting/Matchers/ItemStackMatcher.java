package com.unforbidable.tfc.bids.Core.Crafting.Matchers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;

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
        int oreId = OreDictionary.getOreID(ore);
        return Arrays.stream(OreDictionary.getOreIDs(itemStack))
            .anyMatch(value -> oreId == value);
    }

}
