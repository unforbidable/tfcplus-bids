package com.unforbidable.tfc.bids.Core.Handwork;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HandworkProgress {

    public final ItemStack outputItem;
    public final ItemStack inputItem;
    public final int duration;
    public int stage;

    public HandworkProgress(ItemStack outputItem, ItemStack inputItem, int duration, int initialStage) {
        this.outputItem = outputItem;
        this.inputItem = inputItem;
        this.duration = duration;
        this.stage = initialStage;
    }

    public static HandworkProgress loadItemStackFromNBT(NBTTagCompound tag) {
        ItemStack outputItem = null;
        if (tag.hasKey("outputItem")) {
            NBTTagCompound tagOutputItem = tag.getCompoundTag("outputItem");
            outputItem = ItemStack.loadItemStackFromNBT(tagOutputItem);
        }
        ItemStack inputItem = null;
        if (tag.hasKey("inputItem")) {
            NBTTagCompound tagInputItem = tag.getCompoundTag("inputItem");
            inputItem = ItemStack.loadItemStackFromNBT(tagInputItem);
        }
        int duration = tag.getInteger("duration");
        int stage = tag.getInteger("stage");

        return new HandworkProgress(outputItem, inputItem, duration, stage);
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (outputItem != null) {
            NBTTagCompound tagOutputItem = new NBTTagCompound();
            outputItem.writeToNBT(tagOutputItem);
            tag.setTag("outputItem", tagOutputItem);
        }
        if (inputItem != null) {
            NBTTagCompound tagInputItem = new NBTTagCompound();
            inputItem.writeToNBT(tagInputItem);
            tag.setTag("inputItem", tagInputItem);
        }

        tag.setInteger("duration", duration);
        tag.setInteger("stage", stage);
    }

}
