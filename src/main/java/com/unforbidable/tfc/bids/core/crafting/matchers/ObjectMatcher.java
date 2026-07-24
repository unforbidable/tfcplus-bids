package com.unforbidable.tfc.bids.core.crafting.matchers;

import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ObjectMatcher {

    public abstract boolean is(ItemStack itemStack);

    public abstract boolean is(Item item);

    public abstract boolean is(Block block);

    public abstract boolean is(String ore);

    public boolean isAny(ItemStack... itemStacks) {
        return Arrays.stream(itemStacks)
            .anyMatch(this::is);
    }

    public boolean isAny(Block... blocks) {
        return Arrays.stream(blocks)
            .anyMatch(this::is);
    }

    public boolean isAny(Item... items) {
        return Arrays.stream(items)
            .anyMatch(this::is);
    }

    public boolean isAny(String... ores) {
        return Arrays.stream(ores)
            .anyMatch(this::is);
    }

}
