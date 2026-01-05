package com.unforbidable.tfc.bids.Core.Woodworking.Geometry;

public class ShapeSet {

    private final Shape[] shapes;

    public ShapeSet(Shape[] shapes) {
        this.shapes = shapes;
    }

    public Shape[] getShapes() {
        return shapes;
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
