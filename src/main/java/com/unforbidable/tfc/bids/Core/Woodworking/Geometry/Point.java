package com.unforbidable.tfc.bids.Core.Woodworking.Geometry;

public class Point {

    public static final Point ZERO = Point.at(0, 0);

    public final int x;
    public final int y;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point at(int x, int y) {
        return new Point(x, y);
    }

    public Point flip(Orientation orientation) {
        switch (orientation) {
            case VERTICAL:
                return Point.at(x, -y);
            case HORIZONTAL:
                return Point.at(-x, y);
        }

        return this;
    }

    public Point rotate(int rotation) {
        int num = rotation % 4;
        switch (num) {
            case 1:
                return Point.at(-y, +x);
            case 2:
                return Point.at(-x, -y);
            case 3:
                return Point.at(+y, -x);
        }

        return this;
    }

    public Point offset(int x, int y) {
        return Point.at(this.x + x, this.y + y);
    }

}
