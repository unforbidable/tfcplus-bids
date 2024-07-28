package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IKilnChamber {

    String getName();
    boolean validate();
    List<BlockCoord> getPotteryBlocks();
    TileEntity getChimney();
    boolean isValid();

}
