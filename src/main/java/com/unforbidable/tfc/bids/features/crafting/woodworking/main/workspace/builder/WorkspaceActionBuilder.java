package com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.builder;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingAction;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.Workspace;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspaceAction;

public class WorkspaceActionBuilder {

    private final Workspace workspace;
    private final WoodworkingAction action;

    public WorkspaceActionBuilder(Workspace workspace, WoodworkingAction action) {
        this.workspace = workspace;
        this.action = action;
    }

    public WorkspaceAction at(int x, int y) {
        return new WorkspaceAction(workspace, x, y, action);
    }

}
