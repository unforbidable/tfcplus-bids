package com.unforbidable.tfc.bids.Core.WoodPile;

import java.util.Iterator;

import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class WoodPileBoundsIterator implements Iterable<WoodPileItemBounds> {

    static final int ROWS_PER_PILE = 4;
    static final int ITEMS_PER_ROW = 4;

    final ItemStack[] items;
    final int orientation;

    public WoodPileBoundsIterator(ItemStack[] items, int orientation) {
        this.items = items;
        this.orientation = orientation;
    }

    @Override
    public Iterator<WoodPileItemBounds> iterator() {
        return new Iterator<WoodPileItemBounds>() {

            int index = 0;
            int i = 0;
            int row = 0;
            int rowLargeItemCount = 0;

            @Override
            public boolean hasNext() {
                // The array is the TileEntityWoodPile.storage
                // so it can contain null items
                // that need to be skipped
                while (index < items.length && items[index] == null) {
                    index++;
                }

                return index < items.length && row < ROWS_PER_PILE;
            }

            @Override
            public WoodPileItemBounds next() {
                final ItemStack item = items[index];
                final IWoodPileRenderProvider renderProvider = WoodPileRegistry.findItem(item.getItem());

                final int itemsPerRow = renderProvider.isWoodPileLargeItem(item) ? 2 : 4;
                final double stride = 1f / itemsPerRow;

                final int rowRotation = BidsOptions.WoodPile.rotateItems ? (row % 2) : 0;
                final boolean isRowRotated = rowRotation != (orientation % 2);
                final int rowOrientation = (orientation + rowRotation) % 4;

                AxisAlignedBB bounds;
                switch (rowOrientation) {
                    case 0:
                        bounds = AxisAlignedBB.getBoundingBox((3 - i) * stride, row * stride, 0,
                                (4 - i) * stride, (row + 1) * stride, 1);
                        break;

                    case 1:
                        bounds = AxisAlignedBB.getBoundingBox(0, row * stride, (3 - i) * stride,
                                1, (row + 1) * stride, (4 - i) * stride);
                        break;

                    case 2:
                        bounds = AxisAlignedBB.getBoundingBox(i * stride, row * stride, 0,
                                (i + 1) * stride, (row + 1) * stride, 1);
                        break;

                    default: // 3
                        bounds = AxisAlignedBB.getBoundingBox(0, row * stride, i * stride,
                                1, (row + 1) * stride, (i + 1) * stride);
                        break;
                }

                WoodPileItemBounds woodPileItem = new WoodPileItemBounds(index, item, renderProvider, bounds,
                        isRowRotated);

                // The logic bellow assumes large items are
                // in front of the small ones

                i++;

                if (renderProvider.isWoodPileLargeItem(item)) {
                    rowLargeItemCount++;
                    i++;
                }

                if (i > 3) {
                    row++;
                    i = 0;
                    rowLargeItemCount = 0;

                    if (rowLargeItemCount == 1) {
                        // If one large item then we continue next to it
                        i = 2;
                    } else if (rowLargeItemCount == 2) {
                        // If two large items then we continue above
                        row++;
                    }
                }

                index++;

                return woodPileItem;
            }

        };
    }

}
