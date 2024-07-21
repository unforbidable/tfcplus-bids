package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IKilnChamber {

    String getName();
    boolean validate();
    List<TileEntity> getPottery();
    TileEntity getChimney();
    boolean isValid();

}
