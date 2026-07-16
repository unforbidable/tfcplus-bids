package com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingPlan;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.ShapeSet;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.builder.PlanBuilder;
import java.awt.geom.Area;

public class Plan implements WoodworkingPlan {

    private final String name;
    private final ShapeSet shapes;

    public Plan(String name, ShapeSet shapes) {
        this.name = name;
        this.shapes = shapes;
    }

    public static PlanBuilder create(String name) {
        return new PlanBuilder(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Area getCutout() {
         return shapes.getArea();
    }

    @Override
    public boolean matches(Area cutout) {
        return getCutout().equals(cutout);
    }

}
