package com.unforbidable.tfc.bids.Core.DryingSurface.Render;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingItemRenderInfo;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public class MudBrickRenderInfo implements IDryingItemRenderInfo {

    private static final AxisAlignedBB ON_SIDE = AxisAlignedBB.getBoundingBox(1 / 4f, 0, 1 / 16f, 3 / 4f, 6 / 8f, 15 / 16f);
    private static final AxisAlignedBB ON_BELLY = AxisAlignedBB.getBoundingBox(1 / 8f, 0, 1 / 16f, 7 / 8f, 1 / 2f, 15 / 16f);

    private final boolean drying;

    public MudBrickRenderInfo(boolean drying) {
        this.drying = drying;
    }

    @Override
    public IIcon getRenderIcon(DryingItem dryingItem) {
        return TFCBlocks.dryingBricks.getIcon(0, dryingItem.getCurrentItem().getItemDamage() % 32);
    }

    @Override
    public int getRenderColor(DryingItem dryingItem) {
        if (drying) {
            // slightly less wet (side or belly does not matter)
            return 0xffdfdf9f;
        } else if (dryingItem.getCurrentItem().getItemDamage() >= 32) {
            // wet
            return 0xff9f9f7f;
        } else {
            // dry
            return 0xffffffff;
        }
    }

    @Override
    public AxisAlignedBB getRenderBounds(DryingItem dryingItem) {
        if (drying) {
            // when drying stage 1 is completed, drying mud brick stays on the belly
            // when drying is in progress again for stage 2, place on the side
            return dryingItem.progress == 1 ? ON_BELLY : ON_SIDE;
        } else {
            // Wet mud brick is on the belly when stage 1 is in progress
            // and fully dried mud brick on the side when stage 2 is completed
            return dryingItem.getCurrentItem().getItemDamage() >= 32 ? ON_BELLY : ON_SIDE;
        }
    }

}
