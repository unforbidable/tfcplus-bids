package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.builder;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.ActionGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionGroupBuilder {

    private final String name;
    private final List<WoodworkingAction> actions = new ArrayList<WoodworkingAction>();
    private final Set<String> usage = new HashSet<>();
    private float toolDamage = 0;

    public ActionGroupBuilder(String name) {
        this.name = name;
    }

    public ActionGroupBuilder damage(float toolDamage) {
        this.toolDamage = toolDamage;

        return this;
    }

    public ActionGroupBuilder usage(String usage) {
        this.usage.add(usage);

        return this;
    }

    public ActionGroupBuilder add(WoodworkingAction action) {
        actions.add(action);

        return this;
    }

    public ActionGroup build() {
        return new ActionGroup(name, actions.toArray(new WoodworkingAction[0]), toolDamage, usage);
    }

}
