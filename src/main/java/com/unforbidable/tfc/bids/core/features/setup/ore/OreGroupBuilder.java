package com.unforbidable.tfc.bids.core.features.setup.ore;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreGroupBuilder {

    private final String name;
    private final List<ItemStack> items = new ArrayList<>();

    public OreGroupBuilder(String name) {
        this.name = name;
    }

    public OreGroupBuilder add(ItemStack item) {
        items.add(item);

        return this;
    }

    public OreGroupBuilder add(Item item) {
        return add(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    public OreGroupBuilder add(Item ...items) {
        for (Item item : items) {
            add(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
        }

        return this;
    }

    public OreGroupBuilder add(Block block) {
        return add(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
    }

    public OreGroupBuilder add(Block ...blocks) {
        for (Block block : blocks) {
            add(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
        }

        return this;
    }

    public OreGroup build() {
        return new OreGroup(name, items);
    }

}
