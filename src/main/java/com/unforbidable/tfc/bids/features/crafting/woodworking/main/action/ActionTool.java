package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action;

import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.builder.ActionToolBuilder;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IWoodworkingActionGroup;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IWoodworkingTool;

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
