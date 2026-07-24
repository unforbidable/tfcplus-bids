package com.unforbidable.tfc.bids.features.crafting.woodworking.main.material;

import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterialType;

public class Material implements WoodworkingMaterial {

    private final String oreName;
    private final int workspaceWidth;
    private final int workspaceHeight;
    private final WoodworkingMaterialType type;

    public Material(String oreName, int workspaceWidth, int workspaceHeight, WoodworkingMaterialType type) {
        this.oreName = oreName;
        this.workspaceWidth = workspaceWidth;
        this.workspaceHeight = workspaceHeight;
        this.type = type;
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
    public WoodworkingMaterialType getType() {
        return type;
    }

}
