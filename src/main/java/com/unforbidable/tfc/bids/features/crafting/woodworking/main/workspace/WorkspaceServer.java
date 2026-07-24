package com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.WoodworkingHelper;
import java.awt.geom.Area;
import java.util.List;

public class WorkspaceServer {

    private final WoodworkingMaterial material;

    private final Workspace workspace;

    private final List<WorkspacePlan> plans;

    public WorkspaceServer(WoodworkingMaterial material, List<WorkspacePlan> plans) {
        this.material = material;
        this.plans = plans;

        workspace = new Workspace(0, 0, material.getWorkspaceWidth(), material.getWorkspaceHeight());
    }

    public boolean performAction(String actionName, int x, int y) {
        WoodworkingAction action = WoodworkingHelper.getActionByName(actionName);
        if (action != null) {
            return workspace.action(action).at(x, y).perform();
        }

        return false;
    }

    public WorkspacePlan findMatchingPlan() {
        for (WorkspacePlan plan : plans) {
            if (plan.matches(workspace.getCutout())) {
                return plan;
            }
        }

        return null;
    }

    public void reset() {
        workspace.reset();
    }

    public Area getCutout() {
        return workspace.getCutout();
    }

}
