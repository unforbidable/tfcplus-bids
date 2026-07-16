package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterialType;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionGroup;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.builder.ActionGroupBuilder;
import java.util.EnumSet;

public class ActionGroup implements WoodworkingActionGroup {

    private final String name;
    private final WoodworkingAction[] actions;
    private final float toolDamage;
    private final EnumSet<WoodworkingMaterialType> usage;

    public ActionGroup(String name, WoodworkingAction[] actions, float toolDamage, EnumSet<WoodworkingMaterialType> usage) {
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
    public EnumSet<WoodworkingMaterialType> getUsage() {
        return usage;
    }

}
