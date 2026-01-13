package com.unforbidable.tfc.bids.Core.Woodworking.Geometry;

import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Builders.RectShapeBuilder;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Builders.ShapeBuilder;
import com.unforbidable.tfc.bids.Core.Woodworking.Geometry.Builders.TriShapeBuilder;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingShape;

import java.awt.Polygon;

public class Shape implements IWoodworkingShape {

    public final Point[] points;

    public Shape(Point[] points) {
        this.points = points;
    }

    public static RectShapeBuilder rectFrom(int x, int y) {
        return new RectShapeBuilder(x, y);
    }

    public static TriShapeBuilder triFrom(int x, int y) { return new TriShapeBuilder(x, y); }

    public static Shape pointAt(int x, int y) {
        return new RectShapeBuilder(x, y).size(1, 1);
    }

    public static ShapeBuilder from(int x, int y) {
        return new ShapeBuilder(x, y);
    }

    public Shape flip(Orientation orientation) {
        Point[] flipped = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            flipped[i] = points[i].flip(orientation);
        }

        return new Shape(flipped);
    }

    public Shape rotate(int rotation) {
        Point[] rotated = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            rotated[i] = points[i].rotate(rotation);
        }

        return new Shape(rotated);
    }

    public Shape offset(int x, int y) {
        Point[] offset = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            offset[i] = points[i].offset(x, y);
        }

        return new Shape(offset);
    }

    public boolean isValid() {
        if (points.length < 3) {
            return false;
        } else {
            // check lines point to point
            for (int i = 0; i < points.length - 1; i++) {
                if (!isLineValid(points[i], points[i + 1])) {
                    return false;
                }
            }

            // then check first to last
            return isLineValid(points[0], points[points.length - 1]);
        }
    }

    private boolean isLineValid(Point a, Point b) {
        // Not the same point AND line is (diagonal OR orthogonal)
        return !(a.x == b.x && a.y == b.y) && (a.x == b.x || a.y == b.y || Math.abs(a.x - b.x) == Math.abs(a.y - b.y));
    }

    @Override
    public Polygon getPolygon() {
        int[] xPoints = new int[this.points.length];
        int[] yPoints = new int[this.points.length];
        for (int i = 0; i < this.points.length; i++) {
            xPoints[i] = this.points[i].x;
            yPoints[i] = this.points[i].y;
        }
        return new Polygon(xPoints, yPoints, this.points.length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < points.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(points[i].toString());
        }
        sb.append("]");

        return sb.toString();
    }

}
