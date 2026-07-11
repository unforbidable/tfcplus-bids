package com.unforbidable.tfc.bids.api.features.kiln;

import com.unforbidable.tfc.bids.util.BlockCoord;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface KilnChamber {

    String getName();
    boolean validate();
    List<BlockCoord> getPotteryBlocks();
    TileEntity getChimney();
    boolean isValid();

}
