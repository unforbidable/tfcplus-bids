package com.unforbidable.tfc.bids.core.features.client.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import java.util.function.Supplier;

public class BlockClientSpecBuilder {

    private final String name;
    private Supplier<ISimpleBlockRenderingHandler> blockRender;

    public BlockClientSpecBuilder(String name) {
        this.name = name;
    }

    public BlockClientSpecBuilder render(Supplier<ISimpleBlockRenderingHandler> blockRender) {
        this.blockRender = blockRender;

        return this;
    }

    public BlockClientSpec build() {
        return new BlockClientSpec(name,
            blockRender != null ? blockRender.get() : null);
    }

}
