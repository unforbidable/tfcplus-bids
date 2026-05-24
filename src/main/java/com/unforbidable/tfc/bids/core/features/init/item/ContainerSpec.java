package com.unforbidable.tfc.bids.core.features.init.item;

import net.minecraft.item.Item;

public class ContainerSpec {

    public final Item item;
    public final int emptyItemDamage;

    public ContainerSpec(Item item, int emptyItemDamage) {
        this.item = item;
        this.emptyItemDamage = emptyItemDamage;
    }

}
