package com.unforbidable.tfc.bids.core.features.setup.ore;

import java.util.List;
import net.minecraft.item.ItemStack;

public class OreGroup {

    public final String name;
    public final List<ItemStack> items;

    public OreGroup(String name, List<ItemStack> items) {
        this.name = name;
        this.items = items;
    }

}
