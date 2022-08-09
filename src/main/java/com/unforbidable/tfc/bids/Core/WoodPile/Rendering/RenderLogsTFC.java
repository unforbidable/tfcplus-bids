package com.unforbidable.tfc.bids.Core.WoodPile.Rendering;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class RenderLogsTFC implements IWoodPileRenderProvider {

    @Override
    public boolean isWoodPileLargeItem(ItemStack itemStack) {
        return false;
    }

    @Override
    public void onWoodPileRender(ItemStack itemStack, boolean rotated, IWoodPileRenderer renderer) {
        final boolean isChoppedLog = itemStack.getItemDamage() % 2 == 1;

        // Normal log textures are that of stacked logs with half scale
        for (int i = 0; i < 6; i++) {
            renderer.setTexture(i, getStackedBlockIcon(itemStack, i, rotated));
            renderer.setTextureScale(i, 0.5f);
        }

        if (isChoppedLog) {
            // Top is peeled log
            renderer.setTexture(1, getPeeledLogBlockIcon(itemStack, 1, rotated));

            // One long side is also peeled log
            // And rotate short side to match the bark on the long side
            // Crop short sides
            if (rotated) {
                renderer.setTexture(2, getPeeledLogBlockIcon(itemStack, 2, rotated));
                renderer.setTextureRotation(4, 3);
                renderer.setTextureScale(4, 0.25f);
                renderer.setTextureScale(5, 0.25f);
            } else {
                renderer.setTexture(5, getPeeledLogBlockIcon(itemStack, 5, rotated));
                renderer.setTextureRotation(2, 3);
                renderer.setTextureScale(2, 0.25f);
                renderer.setTextureScale(3, 0.25f);
            }
        }
    }

    private IIcon getPeeledLogBlockIcon(ItemStack itemStack, int side, boolean rotated) {
        final int peeledDamage = itemStack.getItemDamage() / 2;
        final int offset = peeledDamage - peeledDamage % 16;
        final int meta = peeledDamage % 16;

        Block block = rotated ? WoodHelper.getLogWallBlock(offset, 4, false)
                : WoodHelper.getLogWallBlock(offset, 2, true);

        return block.getIcon(side, meta);
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
