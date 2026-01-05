package com.unforbidable.tfc.bids.Core.Woodworking.Actions;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingActionSide;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;

public class Action implements IWoodworkingAction {

    private final String name;
    private final ActionSpec spec;
    private final EnumWoodworkingActionSide side;

    public Action(String name, ActionSpec spec) {
        this(name, spec, EnumWoodworkingActionSide.NONE);
    }

    public Action(String name, ActionSpec spec, EnumWoodworkingActionSide side) {
        this.name = name;
        this.spec = spec;
        this.side = side;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EnumWoodworkingActionSide getSide() {
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
