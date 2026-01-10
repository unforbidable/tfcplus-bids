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
                // Polygons sometimes come out with duplicate fist and last point
                areas.add(reducePolygon(polygon));
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
        int[] xPoints = new int[polygon.npoints];
        int[] yPoints = new int[polygon.npoints];
        int nPoints = 0;

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

            // Add point if not on straight line, or duplicate
            if (((abx * bcy - aby * bcx != 0) || (abx * bcx + aby * bcy <= 0)) && (ax != bx || ay != by)) {
                xPoints[nPoints] = bx;
                yPoints[nPoints] = by;
                nPoints++;
            }
        }

        if (nPoints != polygon.npoints) {
            return new Polygon(xPoints, yPoints, nPoints);
        } else {
            return polygon;
        }
    }

    public static List<Polygon> getScaledAndTranslatedTriangles(Area area, int x, int y, int scale) {
        List<Polygon> triangles = getTriangles(area);

        for (Polygon polygon : triangles) {
            for (int i = 0; i < polygon.npoints; i++) {
                polygon.xpoints[i] = polygon.xpoints[i] * scale + x;
                polygon.ypoints[i] = polygon.ypoints[i] * scale + y;
            }
        }

        return triangles;
    }

    public static List<Polygon> getTriangles(Area area) {
        return getTriangles(getPolygons(area));
    }

    public static List<Polygon> getTriangles(List<Polygon> polygons) {
        List<Polygon> triangles = new ArrayList<Polygon>();

        for (Polygon polygon : polygons) {
            triangulatePolygon(polygon, triangles);
        }

        return triangles;
    }

    public static void triangulatePolygon(Polygon polygon, List<Polygon> triangles) {
        int nPoints = 0;
        int[] xPoints = new int[polygon.npoints];
        int[] yPoints = new int[polygon.npoints];
        int nValidEarsFound = 0;

        Area areaPolygon = new Area(polygon);
        Polygon[] ears = getAllPossibleEars(polygon);
        for (int i = 0; i < ears.length; i++) {
            if (polygon.npoints > nValidEarsFound + 3) {
                Area areaEar = new Area(ears[i]);
                Area areaMerged = new Area(areaPolygon);
                areaMerged.add(areaEar);
                if (areaMerged.equals(areaPolygon)) {
                    // adding proper ear to the polygon doesn't change it
                    triangles.add(ensureTriangleCounterClockwise(ears[i]));
                    nValidEarsFound++;

                    // remove ear to prevent clipping of overlapping ears
                    areaPolygon.subtract(areaEar);

                    // adjacent ears can never be clipped
                    // so skip next ear and simply add the original point
                    if (i < ears.length - 1) {
                        i++;
                    } else {
                        break;
                    }
                }
            }

            // if enough ears clipped or
            // for invalid ears the point stays in the new polygon
            xPoints[nPoints] = polygon.xpoints[i];
            yPoints[nPoints++] = polygon.ypoints[i];
        }

        Polygon newPolygon = reducePolygon(new Polygon(xPoints, yPoints, nPoints));
        // polygon reduction may cause new polygon to have 2 points
        // which represents a line we can simply ignore

        if (newPolygon.npoints > 3) {
            triangulatePolygon(newPolygon, triangles);
        } else if (newPolygon.npoints == 3) {
            triangles.add(ensureTriangleCounterClockwise(newPolygon));
        }
    }

    public static Polygon ensureTriangleCounterClockwise(Polygon triangle) {
        if (isTriangleClockwise(triangle)) {
            int tx = triangle.xpoints[0];
            int ty = triangle.ypoints[0];
            triangle.xpoints[0] = triangle.xpoints[2];
            triangle.ypoints[0] = triangle.ypoints[2];
            triangle.xpoints[2] = tx;
            triangle.ypoints[2] = ty;
        }

        return triangle;
    }

    public static Polygon[] getAllPossibleEars(Polygon polygon) {
        Polygon[] result = new Polygon[polygon.npoints];
        for (int i = 0; i < polygon.npoints; i++) {
            int iPrev = i > 0 ? i - 1 : polygon.npoints - 1;
            int iNext = i < polygon.npoints - 1 ? i + 1 : 0;

            int ax = polygon.xpoints[iPrev];
            int ay = polygon.ypoints[iPrev];
            int bx = polygon.xpoints[i];
            int by = polygon.ypoints[i];
            int cx = polygon.xpoints[iNext];
            int cy = polygon.ypoints[iNext];

            Polygon triangle = new Polygon();
            triangle.addPoint(ax, ay);
            triangle.addPoint(bx, by);
            triangle.addPoint(cx, cy);
            result[i] = triangle;
        }

        return result;
    }

    public static boolean isTriangleClockwise(Polygon polygon) {
        int ax = polygon.xpoints[0];
        int ay = polygon.ypoints[0];
        int bx = polygon.xpoints[1];
        int by = polygon.ypoints[1];
        int cx = polygon.xpoints[2];
        int cy = polygon.ypoints[2];

        return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax) > 0;
    }

}
