package com.unforbidable.tfc.bids.api.features.woodworking;

public interface WoodworkingTool {

    String getOreName();
    int getOffsetX();
    int getOffsetY();
    WoodworkingActionGroup[] getActionGroups();

}
