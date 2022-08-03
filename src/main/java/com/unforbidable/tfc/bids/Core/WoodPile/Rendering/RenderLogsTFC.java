package com.unforbidable.tfc.bids.Core.WoodPile.Rendering;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Items.ItemPeeledLog;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

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
                return ((ItemPeeledLog) BidsItems.peeledLog).getWoodPileIcon(itemStack, side, rotated);
            }

            if (rotated && (side == 4 || side == 5) || !rotated && (side == 2 || side == 3)) {
                // The short sides use thick log icon
                return getThickLogBlockIconTop(itemStack);
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

    private IIcon getThickLogBlockIconTop(ItemStack itemStack) {
        final int offset = itemStack.getItemDamage() - itemStack.getItemDamage() % 16;
        final int meta = itemStack.getItemDamage() % 16;

        switch (offset) {
            case 0:
                return TFCBlocks.logNatural.getIcon(1, meta);
            case 16:
                return TFCBlocks.logNatural2.getIcon(1, meta);
            default: // 32
                return TFCBlocks.logNatural3.getIcon(1, meta);
        }
    }

    private IIcon getStackedBlockIcon(ItemStack itemStack, int side, boolean rotated) {
        final int offset = itemStack.getItemDamage() - itemStack.getItemDamage() % 8;
        final int meta = itemStack.getItemDamage() % 8;

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
    public float getWoodPileIconScale(ItemStack itemStack) {
        return 0.5f;
    }

    @Override
    public boolean isWoodPileLargeItem(ItemStack itemStack) {
        return false;
    }

}
