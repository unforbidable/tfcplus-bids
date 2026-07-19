package com.unforbidable.tfc.bids.api.features.kiln;

import com.unforbidable.tfc.bids.util.BlockCoord;
import java.util.List;
import net.minecraft.tileentity.TileEntity;

public interface KilnChamber {

    String getName();
    boolean validate();
    List<BlockCoord> getPotteryBlocks();
    TileEntity getChimney();
    boolean isValid();

}
