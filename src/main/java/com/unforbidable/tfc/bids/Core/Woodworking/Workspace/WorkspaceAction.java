package com.unforbidable.tfc.bids.Core.Woodworking.Workspace;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingActionSide;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingShape;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;

public class WorkspaceAction {

    public final Workspace workspace;
    public final int x;
    public final int y;
    public final IWoodworkingAction action;

    private Point origin;
    private Area cutout;
    private Area clearance;
    private Area margin;

    public WorkspaceAction(Workspace workspace, int x, int y, IWoodworkingAction action) {
        this.workspace = workspace;
        this.x = x;
        this.y = y;
        this.action = action;
    }

    public boolean canPerform() {
        return workspace.canPerformAction(this);
    }

    public boolean perform() {
        return workspace.performAction(this);
    }

    public EnumWoodworkingActionSide getSide() {
        return action.getSide();
    }

    public Point getOrigin() {
        if (origin != null) {
            return origin;
        }

        origin = new Point(action.getSpec().getOriginX(), action.getSpec().getOriginY());
        origin.translate(x, y);

        return origin;
    }

    public Area getCutout() {
        if (cutout != null) {
            return cutout;
        }

        cutout = new Area();
        for (IWoodworkingShape shape : action.getSpec().getCutoutShapes()) {
            Polygon polygon = shape.getPolygon();
            polygon.translate(x, y);
            cutout.add(new Area(polygon));
        }

        return cutout;
    }

    public Area getClearance() {
        if (clearance != null) {
            return clearance;
        }

        clearance = new Area();
        for (IWoodworkingShape shape : action.getSpec().getClearanceShapes()) {
            Polygon polygon = shape.getPolygon();
            polygon.translate(x, y);
            clearance.add(new Area(polygon));
        }

        return clearance;
    }

    public Area getMargin() {
        if (margin != null) {
            return margin;
        }

        margin = new Area();
        for (IWoodworkingShape shape : action.getSpec().getMarginShapes()) {
            Polygon polygon = shape.getPolygon();
            polygon.translate(x, y);
            margin.add(new Area(polygon));
        }

        return margin;
    }

}
