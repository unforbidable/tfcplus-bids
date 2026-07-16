package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSide;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;

public class Action implements WoodworkingAction {

    private final String name;
    private final ActionSpec spec;
    private final WoodworkingActionSide side;

    public Action(String name, ActionSpec spec) {
        this(name, spec, WoodworkingActionSide.NONE);
    }

    public Action(String name, ActionSpec spec, WoodworkingActionSide side) {
        this.name = name;
        this.spec = spec;
        this.side = side;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public WoodworkingActionSide getSide() {
        return side;
    }

    public ActionSpec getSpec() {
        return spec;
    }

    @Override
    public String toString() {
        return name;
    }

}
