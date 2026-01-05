package com.unforbidable.tfc.bids.Core.Woodworking.Workspace;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingActionSide;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceHelper {

    public static List<Polygon> getPolygons(Area area) {
        PathIterator it = area.getPathIterator(null);
        List<Polygon> areas = new ArrayList<Polygon>();
        Polygon polygon = new Polygon();
        float[] point = new float[2]; //x,y
        while(!it.isDone()) {
            int type = it.currentSegment(point);
            if(type == PathIterator.SEG_MOVETO) {
                polygon.addPoint((int)point[0], (int)point[1]);
            } else if(type == PathIterator.SEG_CLOSE) {
                areas.add(polygon);
                polygon = new Polygon();
            } else {
                polygon.addPoint((int)point[0], (int)point[1]);
            }
            it.next();
        }
        return areas;
    }

    public static Polygon getPolygonClosestToSide(List<Polygon> polygons, EnumWoodworkingActionSide side) {
        while (polygons.size() > 1) {
            Rectangle a = polygons.get(0).getBounds();
            Rectangle b = polygons.get(1).getBounds();
            if (isCloserToSide(a, b, side)) {
                polygons.remove(1);
            } else {
                polygons.remove(0);
            }
        }

        return polygons.get(0);
    }

    private static boolean isCloserToSide(Rectangle a, Rectangle b, EnumWoodworkingActionSide side) {
        switch (side) {
            case TOP:
                return a.y < b.y;
            case BOTTOM:
                return a.y > b.y;
            case LEFT:
                return a.x < b.x;
            case RIGHT:
                return a.x > b.x;
        }

        return true;
    }

    public static Polygon reducePolygon(Polygon polygon) {
        List<Point> points = new ArrayList<Point>();
        for (int i = 0; i < polygon.npoints; i++) {
            int iPrev = i > 0 ? i - 1 : polygon.npoints - 1;
            int iNext = i < polygon.npoints - 1 ? i + 1 : 0;

            int ax = polygon.xpoints[iPrev];
            int ay = polygon.ypoints[iPrev];
            int bx = polygon.xpoints[i];
            int by = polygon.ypoints[i];
            int cx = polygon.xpoints[iNext];
            int cy = polygon.ypoints[iNext];

            int abx = bx - ax;
            int aby = by - ay;
            int bcx = cx - bx;
            int bcy = cy - by;

            if ((abx * bcy - aby * bcx != 0) || (abx * bcx + aby * bcy <= 0)) {
                points.add(new Point(bx, by));
            }
        }

        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xPoints[i] = points.get(i).x;
            yPoints[i] = points.get(i).y;
        }

        return new Polygon(xPoints, yPoints, points.size());
    }

}
