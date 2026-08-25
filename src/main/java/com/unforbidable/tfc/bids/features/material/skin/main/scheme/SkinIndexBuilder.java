package com.unforbidable.tfc.bids.features.material.skin.main.scheme;

import net.minecraft.item.Item;

public class SkinIndexBuilder {

    private final Item item;
    private final String name;
    private SkinFurSpec fur;
    private SkinWoolSpec wool;

    public SkinIndexBuilder(Item item, String name) {
        this.item = item;
        this.name = name;
    }

    public SkinIndexBuilder fur(SkinFurSpec fur) {
        this.fur = fur;

        return this;
    }

    public SkinIndexBuilder wool(SkinWoolSpec wool) {
        this.wool = wool;

        return this;
    }

    public SkinIndex build() {
        return new SkinIndex(item, name, fur, wool);
    }

}
