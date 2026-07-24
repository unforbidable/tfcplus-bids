package com.unforbidable.tfc.bids.api.features.woodworking;

import java.util.EnumSet;

public interface WoodworkingActionGroup {

    String getName();
    WoodworkingAction[] getActions();
    EnumSet<WoodworkingMaterialType> getUsage();
    float getToolDamage();

}
