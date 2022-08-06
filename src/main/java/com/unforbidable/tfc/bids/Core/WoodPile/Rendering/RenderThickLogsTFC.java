package com.unforbidable.tfc.bids.Core.WoodPile.Rendering;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class RenderThickLogsTFC implements IWoodPileRenderProvider {

    @Override
    public IIcon getWoodPileIcon(ItemStack itemStack, int side, boolean rotated) {
        final int damage = itemStack.getItemDamage() / 2;
        final int offset = damage - damage % 8;
        final int meta = damage % 8;

        // Meta +8 indicates rotated stacked logs
        final int rotatedMeta = rotated ? meta + 8 : meta;

        switch (offset) {
            case 0:
                return TFCBlocks.woodHoriz.getIcon(side, rotatedMeta);
            case 8:
                return TFCBlocks.woodHoriz2.getIcon(side, rotatedMeta);
            case 16:
                return TFCBlocks.woodHoriz3.getIcon(side, rotatedMeta);
            case 24:
                return TFCBlocks.woodHoriz4.getIcon(side, rotatedMeta);
            case 32:
                return TFCBlocks.woodHoriz5.getIcon(side, rotatedMeta);
            default: // 48
                return TFCBlocks.woodHoriz6.getIcon(side, rotatedMeta);
        }
    }

    @Override
    public int getWoodPileIconRotation(ItemStack itemStack, int side, boolean rotated) {
        return 0;
    }

    @Override
    public float getWoodPileIconScale(ItemStack itemStack, int side, boolean rotated) {
        return 1f;
    }

    @Override
    public boolean isWoodPileLargeItem(ItemStack itemStack) {
        return true;
    }

}
