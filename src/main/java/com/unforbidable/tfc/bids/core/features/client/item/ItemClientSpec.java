package com.unforbidable.tfc.bids.core.features.client.item;

import net.minecraftforge.client.IItemRenderer;

public class ItemClientSpec {

    public final String name;
    public final IItemRenderer itemRender;

    public ItemClientSpec(String name, IItemRenderer itemRender) {
        this.name = name;
        this.itemRender = itemRender;
    }

}
