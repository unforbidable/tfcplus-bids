package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public interface IDryingItemRenderInfo {

    IIcon getRenderIcon(int side, int damage);
    int getRenderColor(int damage);
    AxisAlignedBB getRenderBounds();

}
