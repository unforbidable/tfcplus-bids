package com.unforbidable.tfc.bids.Core.SoakingSurface;

import net.minecraft.item.ItemStack;

public class SoakingSurfaceSlotProgress {

    public final ItemStack input;
    public final ItemStack result;
    public final float progress;

    public SoakingSurfaceSlotProgress(ItemStack input, ItemStack result, float progress) {
        this.input = input;
        this.result = result;
        this.progress = progress;
    }

}
