package com.unforbidable.tfc.bids.api._obsolete.Interfaces;

import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumWoodworkingActionSide;

public interface IWoodworkingAction {

    String getName();
    IWoodworkingActionSpec getSpec();
    EnumWoodworkingActionSide getSide();

}
