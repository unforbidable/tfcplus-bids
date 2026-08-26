package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionGroup;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.builder.ActionGroupBuilder;
import java.util.Set;

public class ActionGroup implements WoodworkingActionGroup {

    private final String name;
    private final WoodworkingAction[] actions;
    private final float toolDamage;
    private final Set<String> usage;

    public ActionGroup(String name, WoodworkingAction[] actions, float toolDamage, Set<String> usage) {
        this.name = name;
        this.actions = actions;
        this.toolDamage = toolDamage;
        this.usage = usage;
    }

    public static ActionGroupBuilder create(String name) {
        return new ActionGroupBuilder(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public WoodworkingAction[] getActions() {
        return actions;
    }

    @Override
    public float getToolDamage() {
        return toolDamage;
    }

    @Override
    public Set<String> getUsage() {
        return usage;
    }

}
