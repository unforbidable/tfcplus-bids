package com.unforbidable.tfc.bids.Core.Woodworking.Workspace;

import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.Builders.WorkspaceActionBuilder;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.List;

public class Workspace {

    private static final int STICKY_AREA_WIDTH = 4;
    private static final int STICKY_AREA_HEIGHT = 4;

    private final Area master;
    private final Area sticky;
    private final Area cutout;

    public Workspace(int x, int y, int w, int h) {
        master = new Area(new Rectangle(x, y, w, h));
        sticky = new Area(new Rectangle(x + w / 2 - STICKY_AREA_WIDTH / 2,
            y + h / 2 - STICKY_AREA_HEIGHT / 2,
            STICKY_AREA_WIDTH + 1,
            STICKY_AREA_HEIGHT + 1));
        cutout = new Area();
    }

    public WorkspaceActionBuilder at(int x, int y) {
        return new WorkspaceActionBuilder(this, x, y);
    }

    public boolean canPerformAction(WorkspaceAction action) {
        // Ensure there is anything left to cut
        return hasClearanceAndMargin(action)
            && !getEffectiveCutoutArea(action).isEmpty();
    }

    public boolean hasClearanceAndMargin(WorkspaceAction action) {
        // Ensure there is clearance
        if (!isAreaCleared(action.getClearance())) {
            return false;
        }

        // Ensure there is margin
        if (!isAreaUntouched(action.getMargin())) {
            return false;
        }

        return true;
    }

    public boolean performAction(WorkspaceAction action) {
        if (hasClearanceAndMargin(action)) {
            Area cut = getEffectiveCutoutArea(action);
            if (!cut.isEmpty()) {
                mergeCut(cut);

                tryToRemoveFallingOffParts();

                return true;
            }
        }

        return false;
    }

    private void tryToRemoveFallingOffParts() {
        Area invertedCutout = new Area(master);
        invertedCutout.subtract(cutout);

        if (!invertedCutout.isSingular()) {
            // Getting a list of all remaining parts
            List<Polygon> polygons = WorkspaceHelper.getPolygons(invertedCutout);

            // because the cutout is inverted
            // some polygons represent holes inside material
            // those areas are part of the current cutout
            for (int i = 0; i < polygons.size(); i++) {
                if (isAreaCleared(new Area(polygons.get(i)))) {
                    // This polygon is a hole
                    polygons.remove(i--);
                }
            }

            if (polygons.size() > 1) {
                for (Polygon sub : polygons) {
                    Area subCopy = new Area(sub.getBounds());
                    subCopy.intersect(sticky);
                    if (subCopy.isEmpty()) {
                        // the bounding box of this polygon does not intersect the sticky middle
                        // make it fall off
                        mergeCut(new Area(sub));

                        // try again
                        tryToRemoveFallingOffParts();
                        break;
                    }
                }
            }
        }
    }

    private void mergeCut(Area cut) {
        cutout.add(cut);

        List<Polygon> polygons = WorkspaceHelper.getPolygons(cutout);
        cutout.reset();

        for (Polygon polygon : polygons) {
            Polygon reducedPolygon = WorkspaceHelper.reducePolygon(polygon);
            cutout.add(new Area(reducedPolygon));
        }
    }

    public Area getEffectiveCutoutArea(WorkspaceAction action) {
        Area effectiveCut = new Area(action.getCutout());
        effectiveCut.intersect(master);
        effectiveCut.subtract(cutout);

        if (!effectiveCut.isSingular()) {
            // projected cut area has be split into two or more areas
            // the area closest to the action side is used
            // this handles vertical axe chopping especially
            List<Polygon> polygons = WorkspaceHelper.getPolygons(effectiveCut);
            Polygon polygon = WorkspaceHelper.getPolygonClosestToSide(polygons, action.getSide());
            return new Area(polygon);
        }

        return effectiveCut;
    }

    public boolean isAreaCleared(Area area) {
        if (area.isEmpty()) {
            return true;
        }

        Area areaCopy = new Area(area);
        areaCopy.intersect(master);

        if (areaCopy.isEmpty()) {
            // whole area is outside the master area
            return true;
        }

        areaCopy.add(cutout);

        // areas are equal if the area is already inside
        return areaCopy.equals(cutout);
    }

    public boolean isAreaUntouched(Area area) {
        if (area.isEmpty()) {
            return true;
        }

        Area areaCopy = new Area(area);
        areaCopy.intersect(master);

        if (!areaCopy.equals(area)) {
            // some area is outside the master area
            return false;
        }

        areaCopy.intersect(cutout);

        // no intersection if cropped area is outside the cut area
        return areaCopy.isEmpty();
    }

}
