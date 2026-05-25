package com.unforbidable.tfc.bids.core.features.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import java.util.List;

public class RenderBlockSpec {

    public final ISimpleBlockRenderingHandler renderer;
    public final List<Class<? extends Block>> blocks;

    public RenderBlockSpec(ISimpleBlockRenderingHandler renderer, List<Class<? extends Block>> blocks) {
        this.renderer = renderer;
        this.blocks = blocks;
    }

}
