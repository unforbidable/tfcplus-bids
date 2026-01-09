package com.unforbidable.tfc.bids.Core.Woodworking.Plans;

import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.ShapeSet;
import com.unforbidable.tfc.bids.Core.Woodworking.Plans.Builders.PlanBuilder;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingPlan;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingShape;

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
