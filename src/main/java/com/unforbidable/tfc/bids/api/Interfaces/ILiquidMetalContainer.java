package com.unforbidable.tfc.bids.api.Interfaces;

import com.dunk.tfc.api.Metal;

public interface ILiquidMetalContainer {

    boolean canEjectLiquidMetal(Metal metal, int volume);

    boolean canAcceptLiquidMetal(Metal metal, int volume, float temp);

    int ejectLiquidMetal(Metal metal, int volume);

    int acceptLiquidMetal(Metal metal, int volume, float temp);

    float getLiquidMetalTemp();

    int getLiquidMetalVolume();

}
