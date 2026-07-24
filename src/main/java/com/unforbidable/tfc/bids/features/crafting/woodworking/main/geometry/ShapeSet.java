package com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry;

import java.awt.geom.Area;

public class ShapeSet {

    private final Shape[] shapes;

    private Area area;

    public ShapeSet(Shape[] shapes) {
        this.shapes = shapes;
    }

    public Area getArea() {
        if (area == null) {
            area = new Area();
            for (Shape shape : shapes) {
                area.add(new Area(shape.getPolygon()));
            }
        }

        return area;
    }

    public ShapeSet flip(Orientation orientation) {
        Shape[] flipped = new Shape[shapes.length];
        for (int i = 0; i < shapes.length; i++) {
            flipped[i] = shapes[i].flip(orientation);
        }
        return new ShapeSet(flipped);
    }

    public ShapeSet rotate(int rotation) {
        Shape[] rotated = new Shape[shapes.length];
        for (int i = 0; i < shapes.length; i++) {
            rotated[i] = shapes[i].rotate(rotation);
        }
        return new ShapeSet(rotated);
    }

}
