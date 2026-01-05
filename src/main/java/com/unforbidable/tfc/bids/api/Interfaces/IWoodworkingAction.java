package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingActionSide;

public interface IWoodworkingAction {

    String getName();
    IWoodworkingActionSpec getSpec();
    EnumWoodworkingActionSide getSide();

}
