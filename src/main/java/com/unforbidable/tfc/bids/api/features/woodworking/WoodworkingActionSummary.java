package com.unforbidable.tfc.bids.api.features.woodworking;

public class WoodworkingActionSummary {

    public final String actionName;
    public final int count;

    public WoodworkingActionSummary(String actionName, int count) {
        this.actionName = actionName;
        this.count = count;
    }

}
