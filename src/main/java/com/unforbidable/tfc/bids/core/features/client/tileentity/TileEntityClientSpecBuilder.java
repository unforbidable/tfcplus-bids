package com.unforbidable.tfc.bids.core.features.client.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

public class TileEntityClientSpecBuilder {

    private final Class<? extends TileEntity> type;
    private Supplier<TileEntitySpecialRenderer> tileEntitySpecialRenderer;

    public TileEntityClientSpecBuilder(Class<? extends TileEntity> type) {
        this.type = type;
    }

    public TileEntityClientSpecBuilder render(Supplier<TileEntitySpecialRenderer> blockRender) {
        this.tileEntitySpecialRenderer = blockRender;

        return this;
    }

    public TileEntityClientSpec build() {
        return new TileEntityClientSpec(type, tileEntitySpecialRenderer.get());
    }

}
