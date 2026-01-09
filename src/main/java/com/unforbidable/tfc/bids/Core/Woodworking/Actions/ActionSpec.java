package com.unforbidable.tfc.bids.Core.Woodworking.Actions;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.Builders.ActionSpecBuilder;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Orientation;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.PointF;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.ShapeSet;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingActionSpec;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingShape;

public class ActionSpec implements IWoodworkingActionSpec {

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
    public IWoodworkingShape[] getCutoutShapes() {
        return cutout.getShapes();
    }

    @Override
    public IWoodworkingShape[] getClearanceShapes() {
        return clearance.getShapes();
    }

    @Override
    public IWoodworkingShape[] getMarginShapes() {
        return margin.getShapes();
    }

}

