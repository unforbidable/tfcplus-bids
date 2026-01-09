package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroupUsage;

public interface IWoodworkingActionGroup {

    IWoodworkingAction[] getActions();
    ActionGroupUsage getUsage();

}
