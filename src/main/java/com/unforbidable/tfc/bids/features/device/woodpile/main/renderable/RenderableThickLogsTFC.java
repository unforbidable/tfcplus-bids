package com.unforbidable.tfc.bids.features.device.woodpile.main.renderable;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderConfigurator;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class RenderableThickLogsTFC implements WoodpileRenderable {

    @Override
    public boolean renderAsLargeWoodpileItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public void configureWoodpileRenderer(ItemStack itemStack, boolean rotated, WoodpileRenderConfigurator renderer) {
        for (int i = 0; i < 6; i++) {
            renderer.setTexture(i, getStackedBlockIcon(itemStack, i, rotated));
        }
    }

    private IIcon getStackedBlockIcon(ItemStack itemStack, int side, boolean rotated) {
        final int stackedDamage = itemStack.getItemDamage();
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
