package com.unforbidable.tfc.bids.Core.Woodworking.Actions.Builders;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroup;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingMaterialType;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;

import java.util.*;

public class ActionGroupBuilder {

    private final String name;
    private final List<IWoodworkingAction> actions = new ArrayList<IWoodworkingAction>();
    private final EnumSet<EnumWoodworkingMaterialType> usage = EnumSet.noneOf(EnumWoodworkingMaterialType.class);
    private float toolDamage = 0;

    public ActionGroupBuilder(String name) {
        this.name = name;
    }

    public ActionGroupBuilder damage(float toolDamage) {
        this.toolDamage = toolDamage;

        return this;
    }

    public ActionGroupBuilder usage(EnumWoodworkingMaterialType usage) {
        this.usage.add(usage);

        return this;
    }

    public ActionGroupBuilder add(IWoodworkingAction action) {
        actions.add(action);

        return this;
    }

    public ActionGroup build() {
        return new ActionGroup(name, actions.toArray(new IWoodworkingAction[0]), toolDamage, usage);
    }

}
