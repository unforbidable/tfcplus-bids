package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action;

import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.builder.ActionToolBuilder;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionGroup;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingTool;

public class ActionTool implements WoodworkingTool {

    private final String oreName;
    private final int offsetX;
    private final int offsetY;
    private final WoodworkingActionGroup[] actionGroups;

    public ActionTool(String oreName, int offsetX, int offsetY, WoodworkingActionGroup[] actionGroups) {
        this.oreName = oreName;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.actionGroups = actionGroups;
    }

    public static ActionToolBuilder create() {
        return new ActionToolBuilder();
    }

    @Override
    public String getOreName() {
        return oreName;
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
    public WoodworkingActionGroup[] getActionGroups() {
        return actionGroups;
    }

}
