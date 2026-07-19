package com.unforbidable.tfc.bids.core.features.client.render;

import java.util.List;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderTileEntitySpec {

    public final TileEntitySpecialRenderer renderer;
    public final List<Class<? extends TileEntity>> tileEntities;

    public RenderTileEntitySpec(TileEntitySpecialRenderer renderer, List<Class<? extends TileEntity>> tileEntities) {
        this.renderer = renderer;
        this.tileEntities = tileEntities;
    }

}
