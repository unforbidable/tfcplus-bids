package com.unforbidable.tfc.bids.Core.Woodworking.Actions.Builders;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroup;
import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroupUsage;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;

import java.util.ArrayList;
import java.util.List;

public class ActionGroupBuilder {

    private final String name;
    private final List<IWoodworkingAction> actions = new ArrayList<IWoodworkingAction>();
    private ActionGroupUsage usage = ActionGroupUsage.ANY;

    public ActionGroupBuilder(String name) {
        this.name = name;
    }

    public ActionGroupBuilder usage(ActionGroupUsage usage) {
        this.usage = usage;

        return this;
    }

    public ActionGroupBuilder add(IWoodworkingAction action) {
        actions.add(action);

        return this;
    }

    public ActionGroup build() {
        return new ActionGroup(name, actions.toArray(new IWoodworkingAction[0]), usage);
    }

}
