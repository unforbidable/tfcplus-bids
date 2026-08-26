package com.unforbidable.tfc.bids.features.crafting.woodworking.main.material;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;

public class Material implements WoodworkingMaterial {

    private final String oreName;
    private final int workspaceWidth;
    private final int workspaceHeight;
    private final String materialName;

    public Material(String oreName, int workspaceWidth, int workspaceHeight, String materialName) {
        this.oreName = oreName;
        this.workspaceWidth = workspaceWidth;
        this.workspaceHeight = workspaceHeight;
        this.materialName = materialName;
    }

    @Override
    public String getOreName() {
        return oreName;
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
    public String getMaterialName() {
        return materialName;
    }

}
