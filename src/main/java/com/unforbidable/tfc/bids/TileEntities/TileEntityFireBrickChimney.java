package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.TileEntities.TEChimney;
import com.unforbidable.tfc.bids.api.Interfaces.IChimney;

public class TileEntityFireBrickChimney extends TEChimney implements IChimney {

    @Override
    public int getChimneyTier() {
        return 2;
    }

    @Override
    public void setChimneySmoke(int smoke) {
        this.smoking = smoke;
    }

    @Override
    public int getChimneySmoke() {
        return this.smoking;
    }

    @Override
    public void setChimneyFire(int fire) {
        this.onFire = fire;
    }

    @Override
    public int getChimneyFire() {
        return this.onFire;
    }

}
