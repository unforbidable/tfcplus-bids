package com.unforbidable.tfc.bids.api.features.woodworking;

import java.util.Set;

public interface WoodworkingActionGroup {

    String getName();
    WoodworkingAction[] getActions();
    Set<String> getUsage();
    float getToolDamage();

}
