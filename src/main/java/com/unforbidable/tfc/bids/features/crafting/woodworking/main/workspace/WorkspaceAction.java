package com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSide;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class WorkspaceAction {

    public final Workspace workspace;
    public final int x;
    public final int y;
    public final WoodworkingAction action;

    private Area cutout;
    private Area clearance;
    private Area margin;

    public WorkspaceAction(Workspace workspace, int x, int y, WoodworkingAction action) {
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

    public Area getEffectiveCutoutArea() {
        return workspace.getEffectiveCutoutArea(this);
    }

    public WoodworkingActionSide getSide() {
        return action.getSide();
    }

    public Area getCutout() {
        if (cutout == null) {
            cutout = new Area(action.getSpec().getCutout());
            cutout.transform(AffineTransform.getTranslateInstance(x, y));
        }

        return cutout;
    }

    public Area getClearance() {
        if (clearance == null) {
            clearance = new Area(action.getSpec().getClearance());
            clearance.transform(AffineTransform.getTranslateInstance(x, y));
        }

        return clearance;
    }

    public Area getMargin() {
        if (margin == null) {
            margin = new Area(action.getSpec().getMargin());
            margin.transform(AffineTransform.getTranslateInstance(x, y));
        }

        return margin;
    }

}
