package com.unforbidable.tfc.bids.Core.Woodworking.Actions;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.Builders.ActionToolBuilder;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingActionGroup;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingTool;

public class ActionTool implements IWoodworkingTool {

    private final int offsetX;
    private final int offsetY;
    private final IWoodworkingActionGroup[] actionGroups;

    public ActionTool(int offsetX, int offsetY, IWoodworkingActionGroup[] actionGroups) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.actionGroups = actionGroups;
    }

    public static ActionToolBuilder create() {
        return new ActionToolBuilder();
    }

    @Override
    public int getOffsetX() {
        return offsetX;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    @Override
    public IWoodworkingActionGroup[] getActionGroups() {
        return actionGroups;
    }

}
