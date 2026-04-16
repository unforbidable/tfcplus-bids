package com.unforbidable.tfc.bids.Core.DryingSurface.Render;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingItemRenderInfo;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public class SoapRenderInfo implements IDryingItemRenderInfo {

    private final boolean cured;

    public SoapRenderInfo(boolean cured) {
        this.cured = cured;
    }

    @Override
    public IIcon getRenderIcon(DryingItem dryingItem) {
        return TFCBlocks.plasteredBlock.getIcon(0, 0);
    }

    @Override
    public int getRenderColor(DryingItem dryingItem) {
        return cured ? 0xffc6ab80 : 0xffe8a540;
    }

    @Override
    public AxisAlignedBB getRenderBounds(DryingItem dryingItem) {
        return AxisAlignedBB.getBoundingBox(1 / 4f, 0, 1 / 8f, 3 / 4f, 1 / 4f, 7 / 8f);
    }

}
