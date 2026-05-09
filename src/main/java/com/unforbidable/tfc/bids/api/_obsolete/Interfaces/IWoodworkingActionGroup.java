package com.unforbidable.tfc.bids.api._obsolete.Interfaces;

import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumWoodworkingMaterialType;

import java.util.EnumSet;

public interface IWoodworkingActionGroup {

    String getName();
    IWoodworkingAction[] getActions();
    EnumSet<EnumWoodworkingMaterialType> getUsage();
    float getToolDamage();

}
