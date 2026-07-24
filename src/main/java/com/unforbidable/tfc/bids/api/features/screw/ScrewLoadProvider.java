package com.unforbidable.tfc.bids.api.features.screw;

import net.minecraftforge.common.util.ForgeDirection;

public interface ScrewLoadProvider {

    float getLoadForScrewInDirection(ForgeDirection direction);

}
