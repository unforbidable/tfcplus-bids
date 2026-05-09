package com.unforbidable.tfc.bids.core.features.client.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class BlockClientSpec {

    public final String name;
    public final ISimpleBlockRenderingHandler blockRender;

    public BlockClientSpec(String name, ISimpleBlockRenderingHandler blockRender) {
        this.name = name;
        this.blockRender = blockRender;
    }

}
