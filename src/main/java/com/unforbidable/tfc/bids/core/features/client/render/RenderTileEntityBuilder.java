package com.unforbidable.tfc.bids.core.features.client.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderTileEntityBuilder {

    private final TileEntitySpecialRenderer renderer;
    private final List<Class<? extends TileEntity>> items = new ArrayList<>();

    public RenderTileEntityBuilder(TileEntitySpecialRenderer renderer) {
        this.renderer = renderer;
    }

    public RenderTileEntityBuilder tileEntity(Class<? extends TileEntity> tileEntity) {
        items.add(tileEntity);

        return this;
    }

    public RenderTileEntitySpec build() {
        return new RenderTileEntitySpec(renderer, items);
    }

}
