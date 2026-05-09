package com.unforbidable.tfc.bids.core.features.client.item;

import net.minecraftforge.client.IItemRenderer;

public class ItemClientSpecBuilder {

    private final String name;
    private IItemRenderer itemRender;

    public ItemClientSpecBuilder(String name) {
        this.name = name;
    }

    public ItemClientSpecBuilder render(IItemRenderer itemRender) {
        this.itemRender = itemRender;

        return this;
    }

    public ItemClientSpec build() {
        return new ItemClientSpec(name, itemRender);
    }

}

