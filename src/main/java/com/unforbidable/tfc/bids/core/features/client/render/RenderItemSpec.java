package com.unforbidable.tfc.bids.core.features.client.render;

import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import java.util.List;

public class RenderItemSpec {

    public final IItemRenderer renderer;
    public final List<Item> items;

    public RenderItemSpec(IItemRenderer renderer, List<Item> items) {
        this.renderer = renderer;
        this.items = items;
    }

}
