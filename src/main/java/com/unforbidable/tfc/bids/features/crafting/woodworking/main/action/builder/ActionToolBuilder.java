package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.builder;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionGroup;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionTool;
import java.util.ArrayList;
import java.util.List;

public class ActionToolBuilder {

    private String oreName;
    private int offsetX;
    private int offsetY;
    private final List<WoodworkingActionGroup> actionGroups = new ArrayList<WoodworkingActionGroup>();

    public ActionToolBuilder() {
    }

    public ActionToolBuilder ore(String oreName) {
        this.oreName = oreName;

        return this;
    }

    public ActionToolBuilder offset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        return this;
    }

    public ActionToolBuilder addActions(WoodworkingActionGroup group) {
        this.actionGroups.add(group);

        return this;
    }

    public ActionTool build() {
        return new ActionTool(oreName, offsetX, offsetY, actionGroups.toArray(new WoodworkingActionGroup[0]));
    }

}
