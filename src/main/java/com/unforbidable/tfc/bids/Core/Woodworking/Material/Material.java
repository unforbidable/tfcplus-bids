package com.unforbidable.tfc.bids.Core.Woodworking.Material;

import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;

public class Material implements IWoodworkingMaterial {

    private final int workspaceWidth;
    private final int workspaceHeight;
    private final boolean flat;

    public Material(int workspaceWidth, int workspaceHeight, boolean flat) {
        this.workspaceWidth = workspaceWidth;
        this.workspaceHeight = workspaceHeight;
        this.flat = flat;
    }

    @Override
    public int getWorkspaceWidth() {
        return workspaceWidth;
    }

    @Override
    public int getWorkspaceHeight() {
        return workspaceHeight;
    }

    @Override
    public boolean isFlat() {
        return flat;
    }

}
