package com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Builders;

import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Point;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Shape;

import java.util.ArrayList;
import java.util.List;

public class RectShapeBuilder {

    private final List<Point> points = new ArrayList<Point>();

    public RectShapeBuilder(int x, int y) {
        this.points.add(Point.at(x, y));
    }

    public Shape to(int x, int y) {
        this.points.add(Point.at(x, this.points.get(0).y));
        this.points.add(Point.at(x, y));
        this.points.add(Point.at(this.points.get(0).x, y));
        return new Shape(points.toArray(new Point[] {}));
    }

    public Shape size(int w, int h) {
        this.points.add(Point.at(this.points.get(0).x + w, this.points.get(0).y));
        this.points.add(Point.at(this.points.get(0).x + w, this.points.get(0).y + h));
        this.points.add(Point.at(this.points.get(0).x, this.points.get(0).y + h));
        return new Shape(points.toArray(new Point[] {}));
    }

}
