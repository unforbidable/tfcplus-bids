package com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan;

import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.ShapeSet;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.builder.PlanBuilder;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IWoodworkingPlan;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.IWoodworkingShape;

public class Plan implements IWoodworkingPlan {

    private final ShapeSet cutout;

    public Plan(ShapeSet cutout) {
        this.cutout = cutout;
    }

    public static PlanBuilder create() {
        return new PlanBuilder();
    }

    @Override
    public IWoodworkingShape[] getCutoutShapes() {
        return cutout.getShapes();
    }

}
