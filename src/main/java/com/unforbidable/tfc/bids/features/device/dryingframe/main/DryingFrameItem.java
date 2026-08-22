package com.unforbidable.tfc.bids.features.device.dryingframe.main;

import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DryingFrameItem extends DryingItem {

    public ItemStack tyingItem;

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (tyingItem != null) {
            NBTTagCompound tagItem = new NBTTagCompound();
            tyingItem.writeToNBT(tagItem);
            tag.setTag("tyingItem", tagItem);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("tyingItem")) {
            NBTTagCompound tagItem = tag.getCompoundTag("tyingItem");
            tyingItem = ItemStack.loadItemStackFromNBT(tagItem);
        } else {
            tyingItem = null;
        }
    }

    public static DryingFrameItem loadDryingFrameItemFromNBT(NBTTagCompound tag) {
        DryingFrameItem dryingFrameItem = new DryingFrameItem();
        dryingFrameItem.readFromNBT(tag);
        return dryingFrameItem;
    }

}
