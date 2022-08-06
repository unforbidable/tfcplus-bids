package com.unforbidable.tfc.bids.Core.WoodPile.Rendering;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class RenderLogsTFC implements IWoodPileRenderProvider {

    @Override
    public IIcon getWoodPileIcon(ItemStack itemStack, int side, boolean rotated) {
        final boolean isChoppedLog = itemStack.getItemDamage() % 2 == 1;

        if (isChoppedLog) {
            if (side == 1 || rotated && side == 2 || !rotated && side == 4) {
                // Top and one of the long sides
                // use peeled log icon (i.e. log wall)
                return getPeeledLogBlockIcon(itemStack, side, rotated);
            }
        }

        return getStackedBlockIcon(itemStack, side, rotated);
    }

    @Override
    public int getWoodPileIconRotation(ItemStack itemStack, int side, boolean rotated) {
        final boolean isChoppedLog = itemStack.getItemDamage() % 2 == 1;

        if (isChoppedLog) {
            // Rotate short sides to match the bark on the long side
            if (rotated && side == 4) {
                return 3;
            } else if (!rotated && side == 3) {
                return 3;
            }
        }

        return 0;
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

    @Override
    public float getWoodPileIconScale(ItemStack itemStack, int side, boolean rotated) {
        final boolean isChoppedLog = itemStack.getItemDamage() % 2 == 1;

        if (isChoppedLog) {
            // Crop short sides
            if (rotated && (side == 4 || side == 5)) {
                return 0.25f;
            } else if (!rotated && (side == 2 || side == 3)) {
                return 0.25f;
            }

            return 0.5f;
        }

        return 0.5f;
    }

    @Override
    public boolean isWoodPileLargeItem(ItemStack itemStack) {
        return false;
    }

}
