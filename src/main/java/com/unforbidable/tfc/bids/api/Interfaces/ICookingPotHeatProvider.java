package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;

public interface ICookingPotHeatProvider {
    EnumCookingHeatLevel getHeatLevel();
}
