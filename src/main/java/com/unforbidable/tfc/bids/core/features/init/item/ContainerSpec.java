package com.unforbidable.tfc.bids.core.features.init.item;

import java.util.function.Supplier;
import net.minecraft.item.Item;

public class ContainerSpec {

    public final Supplier<Item> item;
    public final int emptyItemDamage;

    public ContainerSpec(Supplier<Item> item, int emptyItemDamage) {
        this.item = item;
        this.emptyItemDamage = emptyItemDamage;
    }

}
