package com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Builders;

import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Point;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Shape;

import java.util.ArrayList;
import java.util.List;

public class ShapeBuilder {

    private final List<Point> points = new ArrayList<Point>();

    public ShapeBuilder(int x, int y) {
        this.points.add(Point.at(x, y));
    }

    public ShapeBuilder to(int x, int y) {
        this.points.add(Point.at(x, y));
        return this;
    }

    public Shape build() {
        return new Shape(points.toArray(new Point[] {}));
    }

}
