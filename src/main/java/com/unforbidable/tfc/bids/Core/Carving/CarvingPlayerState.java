package com.unforbidable.tfc.bids.Core.Carving;

import com.unforbidable.tfc.bids.api.Enums.EnumAdzeMode;

public class CarvingPlayerState {

    public EnumAdzeMode adzeMode = EnumAdzeMode.DEFAULT_MODE;
    public boolean isCarvingActive = false;
    public long carvingActivityChangedTime = 0;

}