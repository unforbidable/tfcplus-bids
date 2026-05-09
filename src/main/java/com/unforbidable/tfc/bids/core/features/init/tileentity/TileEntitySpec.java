package com.unforbidable.tfc.bids.core.features.init.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntitySpec {

    public final Class<? extends TileEntity> type;
    public final String id;

    public TileEntitySpec(Class<? extends TileEntity> type, String id) {
        this.type = type;
        this.id = id;
    }

}
