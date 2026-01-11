package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingMaterialType;

import java.util.EnumSet;

public interface IWoodworkingActionGroup {

    String getName();
    IWoodworkingAction[] getActions();
    EnumSet<EnumWoodworkingMaterialType> getUsage();
    float getToolDamage();

}
