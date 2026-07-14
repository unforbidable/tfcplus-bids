package com.unforbidable.tfc.bids.api.features.drying;

import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public interface IDryingItemRenderInfo {

    IIcon getRenderIcon(DryingItem dryingItem);
    int getRenderColor(DryingItem dryingItem);
    AxisAlignedBB getRenderBounds(DryingItem dryingItem);

}
