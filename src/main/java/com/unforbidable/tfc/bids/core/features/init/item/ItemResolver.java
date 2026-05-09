package com.unforbidable.tfc.bids.core.features.init.item;

import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import net.minecraft.item.Item;

import java.util.function.Function;

public class ItemResolver {

    private final Function<FeatureRegistryLookup, Item> supplier;
    private Item item;

    private ItemResolver(Function<FeatureRegistryLookup, Item> supplier, Item item) {
        this.supplier = supplier;
        this.item = item;
    }

    public static ItemResolver of(Item item) {
        return new ItemResolver(l -> item, item);
    }

    public static ItemResolver of(String name) {
        return new ItemResolver(l -> l.item(name), null);
    }

    public Item get(FeatureRegistryLookup lookup) {
        if (item == null) {
            item = supplier.apply(lookup);
        }

        return item;
    }

}
