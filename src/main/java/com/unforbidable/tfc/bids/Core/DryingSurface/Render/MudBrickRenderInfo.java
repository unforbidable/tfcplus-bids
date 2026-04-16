package com.unforbidable.tfc.bids.Core.DryingSurface.Render;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingItemRenderInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public class MudBrickRenderInfo implements IDryingItemRenderInfo {

    private final boolean drying;

    public MudBrickRenderInfo(boolean drying) {
        this.drying = drying;
    }

    @Override
    public IIcon getRenderIcon(ItemStack itemStack) {
        return TFCBlocks.dryingBricks.getIcon(0, itemStack.getItemDamage() % 32);
    }

    @Override
    public int getRenderColor(ItemStack itemStack) {
        return drying || itemStack.getItemDamage() >= 32 ? 0xff9f9f7f : 0xffffffff;
    }

    @Override
    public AxisAlignedBB getRenderBounds(ItemStack itemStack) {
        return drying || itemStack.getItemDamage() < 32 ?
            AxisAlignedBB.getBoundingBox(1 / 4f, 0, 1 / 16f, 3 / 4f, 6 / 8f, 15 / 16f) :
            AxisAlignedBB.getBoundingBox(1 / 8f, 0, 1 / 16f, 7 / 8f, 1 / 2f, 15 / 16f);
    }

}
