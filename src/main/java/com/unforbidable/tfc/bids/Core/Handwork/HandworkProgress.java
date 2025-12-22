package com.unforbidable.tfc.bids.Core.Handwork;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HandworkProgress {

    public final ItemStack outputItem;
    public final int duration;
    public int stage;

    public HandworkProgress(ItemStack outputItem, int duration, int initialStage) {
        this.outputItem = outputItem;
        this.duration = duration;
        this.stage = initialStage;
    }

    public static HandworkProgress loadItemStackFromNBT(NBTTagCompound tag) {
        ItemStack outputItem = null;
        if (tag.hasKey("outputItem")) {
            NBTTagCompound tagOutputItem = tag.getCompoundTag("outputItem");
            outputItem = ItemStack.loadItemStackFromNBT(tagOutputItem);
        }
        int duration = tag.getInteger("duration");
        int stage = tag.getInteger("stage");

        return new HandworkProgress(outputItem, duration, stage);
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (outputItem != null) {
            NBTTagCompound tagOutputItem = new NBTTagCompound();
            outputItem.writeToNBT(tagOutputItem);
            tag.setTag("outputItem", tagOutputItem);
        }

        tag.setInteger("duration", duration);
        tag.setInteger("stage", stage);
    }

}
