package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingMaterialType;

public interface IWoodworkingMaterial {

    int getWorkspaceWidth();
    int getWorkspaceHeight();
    EnumWoodworkingMaterialType getType();

}
