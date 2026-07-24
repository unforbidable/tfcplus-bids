package com.unforbidable.tfc.bids.api.features.carving;

import com.unforbidable.tfc.bids.features.building.carving.main.CarvingBit;
import com.unforbidable.tfc.bids.features.building.carving.main.CarvingBitMap;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;

public interface CarvingMode {

    String getName();

    boolean canCarveBit(CarvingBit selectedBit, int side, CarvingBitMap currentCarvedBits);

    List<CarvingBit> getBitsToCarve(CarvingBit selectedBit, int side, CarvingBitMap currentCarvedBits);

    AxisAlignedBB getSelectedBitBounds(CarvingBit selectedBit, int side);

}
