package com.unforbidable.tfc.bids.Core.Woodworking.Workspace.Builders;

import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;
import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.Workspace;
import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.WorkspaceAction;

public class WorkspaceActionBuilder {

    private final Workspace workspace;
    private final IWoodworkingAction action;

    public WorkspaceActionBuilder(Workspace workspace, IWoodworkingAction action) {
        this.workspace = workspace;
        this.action = action;
    }

    public WorkspaceAction at(int x, int y) {
        return new WorkspaceAction(workspace, x, y, action);
    }

}
