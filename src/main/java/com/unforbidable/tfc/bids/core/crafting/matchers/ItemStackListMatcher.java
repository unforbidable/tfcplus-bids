package com.unforbidable.tfc.bids.core.crafting.matchers;

import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackListMatcher extends ObjectMatcher {

    private final List<ItemStack> items;

    public ItemStackListMatcher(List<ItemStack> ore) {
        this.items = ore;
    }

    @Override
    public boolean is(ItemStack itemStack) {
        return items.stream()
            .anyMatch(i -> OreDictionary.itemMatches(i, itemStack, true));
    }

    @Override
    public boolean is(Item item) {
        ItemStack itemStack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
        return items.stream()
            .anyMatch(i -> OreDictionary.itemMatches(i, itemStack, false));
    }

    @Override
    public boolean is(Block block) {
        ItemStack itemStack = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
        return items.stream()
            .anyMatch(i -> OreDictionary.itemMatches(i, itemStack, false));
    }

    @Override
    public boolean is(String ore) {
        String likelyOreName = OreDictionaryHelper.findLikelyOreName(items);
        return ore.equals(likelyOreName);
    }

}
