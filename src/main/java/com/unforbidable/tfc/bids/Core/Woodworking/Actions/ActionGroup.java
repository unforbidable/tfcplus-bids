package com.unforbidable.tfc.bids.Core.Woodworking.Actions;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.Builders.ActionGroupBuilder;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingMaterialType;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingActionGroup;

import java.util.EnumSet;

public class ActionGroup implements IWoodworkingActionGroup {

    private final String name;
    private final IWoodworkingAction[] actions;
    private final float toolDamage;
    private final EnumSet<EnumWoodworkingMaterialType> usage;

    public ActionGroup(String name, IWoodworkingAction[] actions, float toolDamage, EnumSet<EnumWoodworkingMaterialType> usage) {
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
    public IWoodworkingAction[] getActions() {
        return actions;
    }

    @Override
    public float getToolDamage() {
        return toolDamage;
    }

    @Override
    public EnumSet<EnumWoodworkingMaterialType> getUsage() {
        return usage;
    }

}
