package com.unforbidable.tfc.bids.Core.Common.Bounds;

import net.minecraft.util.AxisAlignedBB;

public class AxleWallBearingBounds {

    private static final double bearingStart = 0.125;
    private static final double bearingEnd = 0.875;
    private static final double bearingHoleStart = 0.3;
    private static final double bearingHoleEnd = 0.7;
    private static final double coverStart = 0;
    private static final double coverEnd = 1;
    private static final double coverHoleStart = 0.25;
    private static final double coverHoleEnd = 0.75;

    private final AxisAlignedBB[] bearing;
    private final AxisAlignedBB[] inning;
    private final AxisAlignedBB[] cover;
    private final AxisAlignedBB total;

    public static AxleWallBearingBounds getBounds(int orientation) {
        return new AxleWallBearingBounds(orientation);
    }

    public AxisAlignedBB[] getBearing() {
        return bearing;
    }

    public AxisAlignedBB getTotal() {
        return total;
    }

    public AxisAlignedBB[] getCover() {
        return cover;
    }

    public AxisAlignedBB[] getInning() {
        return inning;
    }

    private AxleWallBearingBounds(int orientation) {
        bearing = new AxisAlignedBB[4];
        inning = new AxisAlignedBB[4];
        cover = new AxisAlignedBB[4];

        if (orientation == 0) {
            bearing[0] = AxisAlignedBB.getBoundingBox(bearingStart, 0, 0, bearingEnd, bearingHoleStart, 1);
            bearing[1] = AxisAlignedBB.getBoundingBox(bearingStart, bearingHoleEnd, 0, bearingEnd, 1, 1);
            bearing[2] = AxisAlignedBB.getBoundingBox(bearingStart, bearingHoleStart, 0, bearingEnd, bearingHoleEnd, bearingHoleStart);
            bearing[3] = AxisAlignedBB.getBoundingBox(bearingStart, bearingHoleStart, bearingHoleEnd, bearingEnd, bearingHoleEnd, 1);

            inning[0] = AxisAlignedBB.getBoundingBox(bearingStart, 0.25, 0.25, bearingEnd, bearingHoleStart, 0.75);
            inning[1] = AxisAlignedBB.getBoundingBox(bearingStart, bearingHoleEnd, 0.25, bearingEnd, 0.75, 0.75);
            inning[2] = AxisAlignedBB.getBoundingBox(bearingStart, bearingHoleStart, 0.25, bearingEnd, bearingHoleEnd, bearingHoleStart);
            inning[3] = AxisAlignedBB.getBoundingBox(bearingStart, bearingHoleStart, bearingHoleEnd, bearingEnd, bearingHoleEnd, 0.75);

            cover[0] = AxisAlignedBB.getBoundingBox(coverStart, 0, 0, coverEnd, coverHoleStart, 1);
            cover[1] = AxisAlignedBB.getBoundingBox(coverStart, coverHoleEnd, 0, coverEnd, 1, 1);
            cover[2] = AxisAlignedBB.getBoundingBox(coverStart, coverHoleStart, 0, coverEnd, coverHoleEnd, coverHoleStart);
            cover[3] = AxisAlignedBB.getBoundingBox(coverStart, coverHoleStart, coverHoleEnd, coverEnd, coverHoleEnd, 1);

            total = AxisAlignedBB.getBoundingBox(bearingStart, 0, 0f, bearingEnd, 1f, 1f);
        } else if (orientation == 1) {
            bearing[0] = AxisAlignedBB.getBoundingBox(0, 0, bearingStart, 1, bearingHoleStart, bearingEnd);
            bearing[1] = AxisAlignedBB.getBoundingBox(0, bearingHoleEnd, bearingStart, 1, 1, bearingEnd);
            bearing[2] = AxisAlignedBB.getBoundingBox(0, bearingHoleStart, bearingStart, bearingHoleStart, bearingHoleEnd, bearingEnd);
            bearing[3] = AxisAlignedBB.getBoundingBox(bearingHoleEnd, bearingHoleStart, bearingStart, 1, bearingHoleEnd, bearingEnd);

            inning[0] = AxisAlignedBB.getBoundingBox(0.25, 0.25, bearingStart, 0.75, bearingHoleStart, bearingEnd);
            inning[1] = AxisAlignedBB.getBoundingBox(0.25, bearingHoleEnd, bearingStart, 0.75, 0.75, bearingEnd);
            inning[2] = AxisAlignedBB.getBoundingBox(0.25, bearingHoleStart, bearingStart, bearingHoleStart, bearingHoleEnd, bearingEnd);
            inning[3] = AxisAlignedBB.getBoundingBox(bearingHoleEnd, bearingHoleStart, bearingStart, 0.75, bearingHoleEnd, bearingEnd);

            cover[0] = AxisAlignedBB.getBoundingBox(0, 0, coverStart, 1, coverHoleStart, coverEnd);
            cover[1] = AxisAlignedBB.getBoundingBox(0, coverHoleEnd, coverStart, 1, 1, coverEnd);
            cover[2] = AxisAlignedBB.getBoundingBox(0, coverHoleStart, coverStart, coverHoleStart, coverHoleEnd, coverEnd);
            cover[3] = AxisAlignedBB.getBoundingBox(coverHoleEnd, coverHoleStart, coverStart, 1, coverHoleEnd, coverEnd);

            total = AxisAlignedBB.getBoundingBox(0f, 0, bearingStart, 1f, 1f, bearingEnd);
        } else if (orientation == 2) {
            bearing[0] = AxisAlignedBB.getBoundingBox(0, bearingStart, 0, 1, bearingEnd, bearingHoleStart);
            bearing[1] = AxisAlignedBB.getBoundingBox(0, bearingStart, bearingHoleEnd, 1, bearingEnd, 1);
            bearing[2] = AxisAlignedBB.getBoundingBox(0, bearingStart, bearingHoleStart, bearingHoleStart, bearingEnd, bearingHoleEnd);
            bearing[3] = AxisAlignedBB.getBoundingBox(bearingHoleEnd, bearingStart, bearingHoleStart, 1, bearingEnd, bearingHoleEnd);

            inning[0] = AxisAlignedBB.getBoundingBox(0.25, bearingStart, 0.25, 0.75, bearingEnd, bearingHoleStart);
            inning[1] = AxisAlignedBB.getBoundingBox(0.25, bearingStart, bearingHoleEnd, 0.75, bearingEnd, 0.75);
            inning[2] = AxisAlignedBB.getBoundingBox(0.25, bearingStart, bearingHoleStart, bearingHoleStart, bearingEnd, bearingHoleEnd);
            inning[3] = AxisAlignedBB.getBoundingBox(bearingHoleEnd, bearingStart, bearingHoleStart, 0.75, bearingEnd, bearingHoleEnd);

            cover[0] = AxisAlignedBB.getBoundingBox(0, coverStart, 0, 1, coverEnd, coverHoleStart);
            cover[1] = AxisAlignedBB.getBoundingBox(0, coverStart, coverHoleEnd, 1, coverEnd, 1);
            cover[2] = AxisAlignedBB.getBoundingBox(0, coverStart, coverHoleStart, coverHoleStart, coverEnd, coverHoleEnd);
            cover[3] = AxisAlignedBB.getBoundingBox(coverHoleEnd, coverStart, coverHoleStart, 1, coverEnd, coverHoleEnd);

            total = AxisAlignedBB.getBoundingBox(0f, bearingStart, 0f, 1f, bearingEnd, 1f);
        } else {
            bearing[0] = AxisAlignedBB.getBoundingBox(0, bearingStart, 0, bearingHoleStart, bearingEnd, 1);
            bearing[1] = AxisAlignedBB.getBoundingBox(bearingHoleEnd, bearingStart, 0, 1, bearingEnd, 1);
            bearing[2] = AxisAlignedBB.getBoundingBox(bearingHoleStart, bearingStart, 0, bearingHoleEnd, bearingEnd, bearingHoleStart);
            bearing[3] = AxisAlignedBB.getBoundingBox(bearingHoleStart, bearingStart, bearingHoleEnd, bearingHoleEnd, bearingEnd, 1);

            inning[0] = AxisAlignedBB.getBoundingBox(0.25, bearingStart, 0.25, bearingHoleStart, bearingEnd, 0.75);
            inning[1] = AxisAlignedBB.getBoundingBox(bearingHoleEnd, bearingStart, 0.25, 0.75, bearingEnd, 0.75);
            inning[2] = AxisAlignedBB.getBoundingBox(bearingHoleStart, bearingStart, 0.25, bearingHoleEnd, bearingEnd, bearingHoleStart);
            inning[3] = AxisAlignedBB.getBoundingBox(bearingHoleStart, bearingStart, bearingHoleEnd, bearingHoleEnd, bearingEnd, 0.75);

            cover[0] = AxisAlignedBB.getBoundingBox(0, coverStart, 0, coverHoleStart, coverEnd, 1);
            cover[1] = AxisAlignedBB.getBoundingBox(coverHoleEnd, coverStart, 0, 1, coverEnd, 1);
            cover[2] = AxisAlignedBB.getBoundingBox(coverHoleStart, coverStart, 0, coverHoleEnd, coverEnd, coverHoleStart);
            cover[3] = AxisAlignedBB.getBoundingBox(coverHoleStart, coverStart, coverHoleEnd, coverHoleEnd, coverEnd, 1);

            total = AxisAlignedBB.getBoundingBox(0f, bearingStart, 0f, 1f, bearingEnd, 1f);
        }
    }

}
