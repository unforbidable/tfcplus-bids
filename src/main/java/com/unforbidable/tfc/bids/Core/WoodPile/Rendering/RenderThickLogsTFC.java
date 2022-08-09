package com.unforbidable.tfc.bids.Core.WoodPile.Rendering;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class RenderThickLogsTFC implements IWoodPileRenderProvider {

    @Override
    public boolean isWoodPileLargeItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public void onWoodPileRender(ItemStack itemStack, boolean rotated, IWoodPileRenderer renderer) {
        for (int i = 0; i < 6; i++) {
            renderer.setTexture(i, getStackedBlockIcon(itemStack, i, rotated));
            renderer.setTextureScale(i, 0.5f);
        }
    }

    private IIcon getStackedBlockIcon(ItemStack itemStack, int side, boolean rotated) {
        final int stackedDamage = itemStack.getItemDamage() / 2;
        final int offset = stackedDamage - stackedDamage % 8;
        final int meta = stackedDamage % 8;

        // Meta +8 indicates rotated stacked logs
        final int rotatedMeta = rotated ? meta + 8 : meta;

        switch (offset) {
            case 0:
                return TFCBlocks.stackedWoodHoriz.getIcon(side, rotatedMeta);
            case 8:
                return TFCBlocks.stackedWoodHoriz2.getIcon(side, rotatedMeta);
            case 16:
                return TFCBlocks.stackedWoodHoriz3.getIcon(side, rotatedMeta);
            case 24:
                return TFCBlocks.stackedWoodHoriz4.getIcon(side, rotatedMeta);
            case 32:
                return TFCBlocks.stackedWoodHoriz5.getIcon(side, rotatedMeta);
            default: // 48
                return TFCBlocks.stackedWoodHoriz6.getIcon(side, rotatedMeta);
        }
    }

}
