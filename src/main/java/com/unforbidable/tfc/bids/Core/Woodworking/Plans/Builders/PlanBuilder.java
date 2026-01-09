package com.unforbidable.tfc.bids.Core.Woodworking.Plans.Builders;

import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Shape;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.ShapeSet;
import com.unforbidable.tfc.bids.Core.Woodworking.Plans.Plan;

import java.util.ArrayList;
import java.util.List;

public class PlanBuilder {

    private final List<Shape> cutout = new ArrayList<Shape>();

    public PlanBuilder cutout(Shape shape) {
        this.cutout.add(shape);

        return this;
    }

    public Plan build() {
        return new Plan(new ShapeSet(cutout.toArray(new Shape[0])));
    }

}
