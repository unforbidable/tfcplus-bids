package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.api.Interfaces.IChimney;

import net.minecraft.tileentity.TileEntity;

public class TileEntityChimney extends TileEntity implements IChimney {

    public TileEntityChimney() {
        super();
    }

    @Override
    public int getChimneyTier() {
        return 0;
    }

}
