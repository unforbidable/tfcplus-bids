package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroupUsage;

public interface IWoodworkingActionGroup {

    String getName();
    IWoodworkingAction[] getActions();
    ActionGroupUsage getUsage();
    float getToolDamage();

}
