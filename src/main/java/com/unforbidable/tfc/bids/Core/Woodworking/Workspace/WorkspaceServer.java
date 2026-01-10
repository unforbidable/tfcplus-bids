package com.unforbidable.tfc.bids.Core.Woodworking.Workspace;

import com.unforbidable.tfc.bids.Core.Woodworking.Plans.PlanInstance;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;
import com.unforbidable.tfc.bids.api.WoodworkingRegistry;

import java.util.List;

public class WorkspaceServer {

    private final IWoodworkingMaterial material;

    private final Workspace workspace;

    private final List<PlanInstance> plans;

    public WorkspaceServer(IWoodworkingMaterial material, List<PlanInstance> plans) {
        this.material = material;
        this.plans = plans;

        workspace = new Workspace(0, 0, material.getWorkspaceWidth(), material.getWorkspaceHeight());
    }

    public boolean performAction(String actionName, int x, int y) {
        IWoodworkingAction action = WoodworkingRegistry.getActionByName(actionName);
        if (action != null) {
            return workspace.action(action).at(x, y).perform();
        }

        return false;
    }

    public PlanInstance findMatchingPlan() {
        for (PlanInstance plan : plans) {
            if (workspace.getCutout().equals(plan.getCutout())) {
                return plan;
            }
        }

        return null;
    }

    public void reset() {
        workspace.reset();
    }

}
