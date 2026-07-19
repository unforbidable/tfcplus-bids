package com.unforbidable.tfc.bids.core.features.client.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemBuilder {

    private final IItemRenderer renderer;
    private final List<Item> items = new ArrayList<>();

    public RenderItemBuilder(IItemRenderer renderer) {
        this.renderer = renderer;
    }

    public RenderItemBuilder item(Item item) {
        items.add(item);

        return this;
    }

    public RenderItemSpec build() {
        return new RenderItemSpec(renderer, items);
    }

}
