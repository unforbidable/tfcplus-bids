package com.unforbidable.tfc.bids.api.features.crucible;

import com.dunk.tfc.api.Metal;

public interface LiquidMetalContainer {

    boolean canEjectLiquidMetal(Metal metal, int volume);

    boolean canAcceptLiquidMetal(Metal metal, int volume, float temp);

    int ejectLiquidMetal(Metal metal, int volume);

    int acceptLiquidMetal(Metal metal, int volume, float temp);

    float getLiquidMetalTemp();

    int getLiquidMetalVolume();

}
