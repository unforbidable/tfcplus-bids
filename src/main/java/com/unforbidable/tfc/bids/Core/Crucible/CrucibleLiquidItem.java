package com.unforbidable.tfc.bids.Core.Crucible;

import com.dunk.tfc.api.Metal;

public class CrucibleLiquidItem {

    final Metal metal;
    float volume;

    public CrucibleLiquidItem(Metal metal, float volume) {
        this.metal = metal;
        this.volume = volume;
    }

    public Metal getMetal() {
        return metal;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

}
