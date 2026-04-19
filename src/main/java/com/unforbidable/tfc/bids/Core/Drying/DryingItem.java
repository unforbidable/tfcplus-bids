package com.unforbidable.tfc.bids.Core.Drying;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DryingItem {

    public ItemStack inputItem;
    public ItemStack resultItem;
    public float wetness;
    public float progress;
    public float failure;
    public boolean paused;
    public long lastProgressUpdatedTicks;
    public long finishedTicks;

    public boolean isComplete() {
        return progress == 1 || failure == 1;
    }

    public ItemStack getCurrentItem() {
        if (isComplete()) {
            return resultItem;
        } else {
            return inputItem;
        }
    }

    public void updateCurrentItem(ItemStack itemStack) {
        if (isComplete()) {
            resultItem = itemStack;
        } else {
            inputItem = itemStack;
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (inputItem != null) {
            NBTTagCompound tagItem = new NBTTagCompound();
            inputItem.writeToNBT(tagItem);
            tag.setTag("inputItem", tagItem);
        }

        if (resultItem != null) {
            NBTTagCompound tagItem = new NBTTagCompound();
            resultItem.writeToNBT(tagItem);
            tag.setTag("resultItem", tagItem);
        }

        tag.setFloat("wetness", wetness);
        tag.setFloat("progress", progress);
        tag.setFloat("failure", failure);
        tag.setBoolean("paused", paused);
        tag.setLong("lastProgressUpdatedTicks", lastProgressUpdatedTicks);
        tag.setLong("finishedTicks", finishedTicks);
    }

    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("inputItem")) {
            NBTTagCompound tagItem = tag.getCompoundTag("inputItem");
            inputItem = ItemStack.loadItemStackFromNBT(tagItem);
        } else {
            inputItem = null;
        }

        if (tag.hasKey("resultItem")) {
            NBTTagCompound tagItem = tag.getCompoundTag("resultItem");
            resultItem = ItemStack.loadItemStackFromNBT(tagItem);
        } else {
            resultItem = null;
        }

        wetness = tag.getFloat("wetness");
        progress = tag.getFloat("progress");
        failure = tag.getFloat("failure");
        paused = tag.getBoolean("paused");
        lastProgressUpdatedTicks = tag.getLong("lastProgressUpdatedTicks");
        finishedTicks = tag.getLong("finishedTicks");
    }

    public static DryingItem loadDryingItemFromNBT(NBTTagCompound tag) {
        DryingItem dryingItem = new DryingItem();
        dryingItem.readFromNBT(tag);
        return dryingItem;
    }

}
