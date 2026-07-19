package com.unforbidable.tfc.bids.core.crafting.matchers;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeInputMatcher {

    private final List<ObjectMatcher> items;

    public RecipeInputMatcher(List<ObjectMatcher> items) {
        this.items = items;
    }

    public boolean contains(ItemStack itemStack) {
        return items.stream()
            .anyMatch(o -> o.is(itemStack));
    }

    public boolean contains(Item item) {
        return items.stream()
            .anyMatch(o -> o.is(item));
    }

    public boolean contains(Block block) {
        return items.stream()
            .anyMatch(o -> o.is(block));
    }

    public boolean contains(String ore) {
        return items.stream()
            .anyMatch(o -> o.is(ore));
    }

    public boolean containsAny(ItemStack ...itemStacks) {
        return items.stream()
            .anyMatch(o -> o.isAny(itemStacks));
    }

    public boolean containsAny(Item ...items) {
        return this.items.stream()
            .anyMatch(o -> o.isAny(items));
    }

    public boolean containsAny(Block ...blocks) {
        return items.stream()
            .anyMatch(o -> o.isAny(blocks));
    }

    public boolean containsAny(String ...ores) {
        return items.stream()
            .anyMatch(o -> o.isAny(ores));
    }

}
