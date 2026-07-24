package com.unforbidable.tfc.bids.features.device.woodpile.main;

import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.features.device.woodpile.WoodpileConfig;
import com.unforbidable.tfc.bids.features.device.woodpile.WoodpileRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class WoodpileBoundsIterator implements Iterable<WoodpileItemBounds> {

    static final int ROWS_PER_PILE = 4;
    static final int ITEMS_PER_ROW = 4;

    final int orientation;
    final List<IndexedItemStack> sortedItems = new ArrayList<IndexedItemStack>();

    public WoodpileBoundsIterator(ItemStack[] items, int orientation) {
        this.orientation = orientation;

        // The logic bellow assumes large items are
        // in front of the small ones
        // Also skip nulls

        int largeItemsAdded = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                WoodpileRenderable render = WoodpileRegistry.renderable.get(items[i].getItem());
                if (render.renderAsLargeWoodpileItem(items[i])) {
                    // Add after last large item added
                    sortedItems.add(largeItemsAdded++, new IndexedItemStack(i, items[i]));
                } else {
                    // Add to the end
                    sortedItems.add(new IndexedItemStack(i, items[i]));
                }
            }
        }
    }

    @Override
    public Iterator<WoodpileItemBounds> iterator() {
        return new Iterator<WoodpileItemBounds>() {

            int index = 0;
            int i = 0;
            int row = 0;
            int rowLargeItemCount = 0;
            int rowRotation = 0;

            @Override
            public boolean hasNext() {
                return index < sortedItems.size() && row < ROWS_PER_PILE;
            }

            @Override
            public WoodpileItemBounds next() {
                final ItemStack item = sortedItems.get(index).itemStack;
                final WoodpileRenderable renderProvider = WoodpileRegistry.renderable.get(item.getItem());

                final int width = renderProvider.renderAsLargeWoodpileItem(item) ? 2 : 1;
                final double stride = 1f / 4;

                final boolean isRowRotated = (rowRotation % 2) != (orientation % 2);
                final int rowOrientation = (orientation + rowRotation) % 4;

                AxisAlignedBB bounds;
                switch (rowOrientation) {
                    case 0:
                        bounds = AxisAlignedBB.getBoundingBox((4 - i - width) * stride, row * stride, 0,
                                (4 - i) * stride, (row + width) * stride, 1);
                        break;

                    case 1:
                        bounds = AxisAlignedBB.getBoundingBox(0, row * stride, (4 - i - width) * stride,
                                1, (row + width) * stride, (4 - i) * stride);
                        break;

                    case 2:
                        bounds = AxisAlignedBB.getBoundingBox(i * stride, row * stride, 0,
                                (i + width) * stride, (row + width) * stride, 1);
                        break;

                    default: // 3
                        bounds = AxisAlignedBB.getBoundingBox(0, row * stride, i * stride,
                                1, (row + width) * stride, (i + width) * stride);
                        break;
                }

                // Pass the original index value
                WoodpileItemBounds woodPileItem = new WoodpileItemBounds(sortedItems.get(index).index, item,
                        renderProvider, bounds, isRowRotated);

                i++;

                if (renderProvider.renderAsLargeWoodpileItem(item)) {
                    rowLargeItemCount++;
                    i++;
                }

                if (i > 3) {
                    row++;

                    if (rowLargeItemCount == 1) {
                        // If one large item then we continue next to it
                        // And skip row rotation
                        i = 2;
                    } else {
                        i = 0;

                        if (WoodpileConfig.rotateItems) {
                            rowRotation = (rowRotation + 1) % 2;
                        }
                    }

                    if (rowLargeItemCount == 2) {
                        // If two large items then we continue above
                        row++;
                    }

                    rowLargeItemCount = 0;
                }

                index++;

                return woodPileItem;
            }

        };
    }

    static class IndexedItemStack {

        final int index;
        final ItemStack itemStack;

        public IndexedItemStack(int index, ItemStack itemStack) {
            this.index = index;
            this.itemStack = itemStack;
        }

        public int getIndex() {
            return index;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

    }

}
