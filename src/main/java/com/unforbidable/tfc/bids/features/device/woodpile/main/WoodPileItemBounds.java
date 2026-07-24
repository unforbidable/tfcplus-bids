package com.unforbidable.tfc.bids.features.device.woodpile.main;

import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class WoodpileItemBounds {
    private final int index;
    private final ItemStack itemStack;
    private final AxisAlignedBB bounds;
    private final WoodpileRenderable renderProvider;
    private final boolean isRowRotated;

    public WoodpileItemBounds(int index, ItemStack itemStack, WoodpileRenderable renderProvider,
                              AxisAlignedBB bounds, boolean isRowRotated) {
        super();

        this.index = index;
        this.itemStack = itemStack;
        this.bounds = bounds;
        this.renderProvider = renderProvider;
        this.isRowRotated = isRowRotated;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public AxisAlignedBB getBounds() {
        return bounds;
    }

    public WoodpileRenderable getRenderProvider() {
        return renderProvider;
    }

    public boolean isRowRotated() {
        return this.isRowRotated;
    }

}
