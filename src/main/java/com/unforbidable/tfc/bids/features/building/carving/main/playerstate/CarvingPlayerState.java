package com.unforbidable.tfc.bids.features.building.carving.main.playerstate;

import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumAdzeMode;

public class CarvingPlayerState {

    public EnumAdzeMode adzeMode = EnumAdzeMode.DEFAULT_MODE;
    public int carvedSide = 0;
    public boolean isCarvingActive = false;
    public long carvingActivityChangedTime = 0;

}
