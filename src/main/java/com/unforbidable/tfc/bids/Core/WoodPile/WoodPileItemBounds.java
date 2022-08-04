package com.unforbidable.tfc.bids.Core.WoodPile;

import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class WoodPileItemBounds {
    private final int index;
    private final ItemStack itemStack;
    private final AxisAlignedBB bounds;
    private final IWoodPileRenderProvider renderProvider;
    private final boolean isRowRotated;

    public WoodPileItemBounds(int index, ItemStack itemStack, IWoodPileRenderProvider renderProvider,
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

    public IWoodPileRenderProvider getRenderProvider() {
        return renderProvider;
    }

    public boolean isRowRotated() {
        return this.isRowRotated;
    }

}
