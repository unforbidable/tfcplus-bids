package com.unforbidable.tfc.bids.core.features.registry;

import com.unforbidable.tfc.bids.core.features.init.item.ItemSpec;
import net.minecraft.item.Item;

public class ItemRegistryEntry {

    public final ItemSpec<?> spec;
    public final Item instance;

    public ItemRegistryEntry(ItemSpec<?> spec, Item instance) {
        this.spec = spec;
        this.instance = instance;
    }

}
