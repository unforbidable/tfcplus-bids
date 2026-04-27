package com.unforbidable.tfc.bids.Core.Drying;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DryingItem {

    public ItemStack inputItem;
    public ItemStack resultItem;
    public float wetness;
    public float progress;
    public float failure;
    public float smoke;
    public long lastProgressUpdatedTicks;
    public long finishedTicks;
    public long smokedTicks;

    public ItemStack getCurrentItem() {
        if (resultItem != null) {
            return resultItem;
        } else {
            return inputItem;
        }
    }

    public void updateCurrentItem(ItemStack itemStack) {
        if (resultItem != null) {
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
        tag.setFloat("smoke", smoke);
        tag.setLong("lastProgressUpdatedTicks", lastProgressUpdatedTicks);
        tag.setLong("finishedTicks", finishedTicks);
        tag.setLong("smokedTicks", smokedTicks);
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
        smoke = tag.getFloat("smoke");
        lastProgressUpdatedTicks = tag.getLong("lastProgressUpdatedTicks");
        finishedTicks = tag.getLong("finishedTicks");
        smokedTicks = tag.getLong("smokedTicks");
    }

    public static DryingItem loadDryingItemFromNBT(NBTTagCompound tag) {
        DryingItem dryingItem = new DryingItem();
        dryingItem.readFromNBT(tag);
        return dryingItem;
    }

}
