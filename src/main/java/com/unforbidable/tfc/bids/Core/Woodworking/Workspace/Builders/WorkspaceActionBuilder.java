package com.unforbidable.tfc.bids.Core.Woodworking.Workspace.Builders;

import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;
import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.Workspace;
import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.WorkspaceAction;

public class WorkspaceActionBuilder {

    private final Workspace workspace;
    private final int x;
    private final int y;

    public WorkspaceActionBuilder(Workspace workspace, int x, int y) {
        this.workspace = workspace;
        this.x = x;
        this.y = y;
    }

    public WorkspaceAction action(IWoodworkingAction action) {
        return new WorkspaceAction(workspace, x, y, action);
    }

}
