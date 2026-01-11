package com.unforbidable.tfc.bids.Core.Woodworking.Material;

import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingMaterialType;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;

public class Material implements IWoodworkingMaterial {

    private final int workspaceWidth;
    private final int workspaceHeight;
    private final EnumWoodworkingMaterialType type;

    public Material(int workspaceWidth, int workspaceHeight, EnumWoodworkingMaterialType type) {
        this.workspaceWidth = workspaceWidth;
        this.workspaceHeight = workspaceHeight;
        this.type = type;
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
    public EnumWoodworkingMaterialType getType() {
        return type;
    }

}
