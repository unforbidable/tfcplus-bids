package com.unforbidable.tfc.bids.core.features.init.item;

public class ContainerSpec {

    public final ItemResolver item;
    public final int emptyItemDamage;

    public ContainerSpec(ItemResolver item, int emptyItemDamage) {
        this.item = item;
        this.emptyItemDamage = emptyItemDamage;
    }

}
