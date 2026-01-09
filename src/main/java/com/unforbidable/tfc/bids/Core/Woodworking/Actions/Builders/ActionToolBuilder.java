package com.unforbidable.tfc.bids.Core.Woodworking.Actions.Builders;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionTool;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ActionToolBuilder {

    private int offsetX;
    private int offsetY;
    private final List<IWoodworkingActionGroup> actionGroups = new ArrayList<IWoodworkingActionGroup>();

    public ActionToolBuilder offset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        return this;
    }

    public ActionToolBuilder addActions(IWoodworkingActionGroup group) {
        this.actionGroups.add(group);

        return this;
    }

    public ActionTool build() {
        return new ActionTool(offsetX, offsetY, actionGroups.toArray(new IWoodworkingActionGroup[0]));
    }

}
