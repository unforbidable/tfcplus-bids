package com.unforbidable.tfc.bids.features.crafting.handwork.main;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HandworkProgress {

    public final ItemStack resultItem;
    public final ItemStack inputItem;
    public final ItemStack extraResultItem;
    public final int duration;
    public int stage;

    public HandworkProgress(ItemStack inputItem, ItemStack resultItem, ItemStack extraResultItem, int duration, int initialStage) {
        this.resultItem = resultItem;
        this.inputItem = inputItem;
        this.extraResultItem = extraResultItem;
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
        ItemStack extraResultItem = null;
        if (tag.hasKey("extraResultItem")) {
            NBTTagCompound tagExtraResultItem = tag.getCompoundTag("extraResultItem");
            extraResultItem = ItemStack.loadItemStackFromNBT(tagExtraResultItem);
        }
        int duration = tag.getInteger("duration");
        int stage = tag.getInteger("stage");

        return new HandworkProgress(inputItem, outputItem, extraResultItem, duration, stage);
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (resultItem != null) {
            NBTTagCompound tagOutputItem = new NBTTagCompound();
            resultItem.writeToNBT(tagOutputItem);
            tag.setTag("outputItem", tagOutputItem);
        }
        if (inputItem != null) {
            NBTTagCompound tagInputItem = new NBTTagCompound();
            inputItem.writeToNBT(tagInputItem);
            tag.setTag("inputItem", tagInputItem);
        }
        if (extraResultItem != null) {
            NBTTagCompound tagExtraResultItem = new NBTTagCompound();
            extraResultItem.writeToNBT(tagExtraResultItem);
            tag.setTag("extraResultItem", tagExtraResultItem);
        }

        tag.setInteger("duration", duration);
        tag.setInteger("stage", stage);
    }

}
