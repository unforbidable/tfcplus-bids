package com.unforbidable.tfc.bids.Core.Woodworking.Geometry;

public class PointF {

    public static final PointF ZERO = PointF.at(0, 0);

    public final float x;
    public final float y;

    private PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static PointF at(float x, float y) {
        return new PointF(x, y);
    }

    public PointF flip(Orientation orientation) {
        switch (orientation) {
            case VERTICAL:
                return PointF.at(x, -y);
            case HORIZONTAL:
                return PointF.at(-x, y);
        }

        return this;
    }

    public PointF rotate(int rotation) {
        int num = rotation % 4;
        switch (num) {
            case 1:
                return PointF.at(-y, +x);
            case 2:
                return PointF.at(-x, -y);
            case 3:
                return PointF.at(+y, -x);
        }

        return this;
    }

    public PointF offset(int x, int y) {
        return PointF.at(this.x + x, this.y + y);
    }

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }

}
