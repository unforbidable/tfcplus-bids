package com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.builder;

import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.ShapeSet;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import java.util.ArrayList;
import java.util.List;

public class PlanBuilder {

    private final String name;

    public PlanBuilder(String name) {
        this.name = name;
    }

    private final List<Shape> cutout = new ArrayList<>();

    public PlanBuilder cutout(Shape shape) {
        this.cutout.add(shape);

        return this;
    }

    public Plan build() {
        return new Plan(name, new ShapeSet(cutout.toArray(new Shape[0])));
    }

}
