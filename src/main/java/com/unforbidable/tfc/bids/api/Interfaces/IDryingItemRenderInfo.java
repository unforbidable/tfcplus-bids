package com.unforbidable.tfc.bids.api.Interfaces;

import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public interface IDryingItemRenderInfo {

    IIcon getRenderIcon(DryingItem dryingItem);
    int getRenderColor(DryingItem dryingItem);
    AxisAlignedBB getRenderBounds(DryingItem dryingItem);

}
