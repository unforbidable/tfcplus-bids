package com.unforbidable.tfc.bids.features.building.carving.main.playerstate;

import com.unforbidable.tfc.bids.api.features.carving.AdzeMode;

public class CarvingPlayerState {

    public AdzeMode adzeMode = AdzeMode.DEFAULT_MODE;
    public int carvedSide = 0;
    public boolean isCarvingActive = false;
    public long carvingActivityChangedTime = 0;

}
