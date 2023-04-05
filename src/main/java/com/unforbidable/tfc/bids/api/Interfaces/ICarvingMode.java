package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public interface ICarvingMode {

    String getName();
    boolean canCarveBit(CarvingBit selectedBit, CarvingBitMap currentCarvedBits);
    List<CarvingBit> getBitsToCarve(CarvingBit selectedBit, CarvingBitMap currentCarvedBits);
    AxisAlignedBB getSelectedBitBounds(CarvingBit selectedBit);

}
