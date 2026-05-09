package com.unforbidable.tfc.bids.core.features.client.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityClientSpec {

    public final Class<? extends TileEntity> type;
    public final TileEntitySpecialRenderer tileEntitySpecialRender;

    public TileEntityClientSpec(Class<? extends TileEntity> type, TileEntitySpecialRenderer tileEntitySpecialRender) {
        this.type = type;
        this.tileEntitySpecialRender = tileEntitySpecialRender;
    }

}
