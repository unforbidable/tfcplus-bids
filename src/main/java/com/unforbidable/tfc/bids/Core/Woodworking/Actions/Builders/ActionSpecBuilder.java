package com.unforbidable.tfc.bids.Core.Woodworking.Actions.Builders;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionSpec;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.PointF;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Shape;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.ShapeSet;

import java.util.ArrayList;
import java.util.List;

public class ActionSpecBuilder {

    private PointF origin = PointF.ZERO;
    private final List<Shape> cutout = new ArrayList<Shape>();
    private final List<Shape> clearance = new ArrayList<Shape>();
    private final List<Shape> margin = new ArrayList<Shape>();

    public ActionSpecBuilder origin(PointF origin) {
        this.origin = origin;

        return this;
    }

    public ActionSpecBuilder cutout(Shape shape) {
        this.cutout.add(shape);

        return this;
    }

    public ActionSpecBuilder clearance(Shape shape) {
        this.clearance.add(shape);

        return this;
    }

    public ActionSpecBuilder margin(Shape shape) {
        this.margin.add(shape);

        return this;
    }

    public ActionSpec build() {
        return new ActionSpec(origin, new ShapeSet(cutout.toArray(new Shape[0])),
            new ShapeSet(clearance.toArray(new Shape[0])), new ShapeSet(margin.toArray(new Shape[0])));
    }

}
