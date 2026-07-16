package com.unforbidable.tfc.bids.features.crafting.woodworking.main.action;

import com.unforbidable.tfc.bids.features.crafting.woodworking.main.action.builder.ActionSpecBuilder;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Orientation;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.PointF;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.ShapeSet;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSpec;
import java.awt.geom.Area;

public class ActionSpec implements WoodworkingActionSpec {

    private final PointF origin;
    private final ShapeSet cutout;
    private final ShapeSet clearance;
    private final ShapeSet margin;

    public ActionSpec(PointF origin, ShapeSet cutout, ShapeSet clearance, ShapeSet margin) {
        this.origin = origin;
        this.cutout = cutout;
        this.clearance = clearance;
        this.margin = margin;
    }

    public static ActionSpecBuilder create() {
        return new ActionSpecBuilder();
    }

    public ActionSpec flip(Orientation orientation) {
        return new ActionSpec(origin.flip(orientation), cutout.flip(orientation), clearance.flip(orientation), margin.flip(orientation));
    }

    public ActionSpec rotate(int rotation) {
        return new ActionSpec(origin.rotate(rotation), cutout.rotate(rotation), clearance.rotate(rotation), margin.rotate(rotation));
    }

    @Override
    public float getOriginX() {
        return origin.x;
    }

    @Override
    public float getOriginY() {
        return origin.y;
    }

    @Override
    public Area getCutout() {
        return cutout.getArea();
    }

    @Override
    public Area getClearance() {
        return clearance.getArea();
    }

    @Override
    public Area getMargin() {
        return margin.getArea();
    }

}

