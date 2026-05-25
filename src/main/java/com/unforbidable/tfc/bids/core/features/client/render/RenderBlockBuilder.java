package com.unforbidable.tfc.bids.core.features.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;

public class RenderBlockBuilder {

    private final ISimpleBlockRenderingHandler renderer;
    private final List<Class<? extends Block>> blocks = new ArrayList<>();

    public RenderBlockBuilder(ISimpleBlockRenderingHandler renderer) {
        this.renderer = renderer;
    }

    public RenderBlockBuilder block(Class<? extends Block> block) {
        blocks.add(block);

        return this;
    }

    public RenderBlockSpec build() {
        return new RenderBlockSpec(renderer, blocks);
    }

}
