package com.unforbidable.tfc.bids.Core.DryingRack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DryingRackItem {

    public final ItemStack dryingItem;
    public final ItemStack tyingItem;
    public final long dryingStartTicks;
    public final boolean tyingItemUsedUp;

    public DryingRackItem(ItemStack dryingItem, ItemStack tyingItem, long dryingStartTicks, boolean tyingItemUsedUp) {
        this.dryingItem = dryingItem;
        this.tyingItem = tyingItem;
        this.dryingStartTicks = dryingStartTicks;
        this.tyingItemUsedUp = tyingItemUsedUp;
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (dryingItem != null) {
            NBTTagCompound tagDryingItem = new NBTTagCompound();
            dryingItem.writeToNBT(tagDryingItem);
            tag.setTag("dryingItem", tagDryingItem);
        }

        if (tyingItem != null) {
            NBTTagCompound tagTyingItem = new NBTTagCompound();
            tyingItem.writeToNBT(tagTyingItem);
            tag.setTag("tyingItem", tagTyingItem);
        }

        tag.setLong("dryingStartTicks", dryingStartTicks);
        tag.setBoolean("tyingItemUsedUp", tyingItemUsedUp);
    }

    public static DryingRackItem loadItemFromNBT(NBTTagCompound tag) {
        ItemStack dryingItem = null;
        ItemStack tyingItem = null;

        if (tag.hasKey("dryingItem")) {
            NBTTagCompound tagDryingItem = tag.getCompoundTag("dryingItem");
            dryingItem = ItemStack.loadItemStackFromNBT(tagDryingItem);
        }

        if (tag.hasKey("tyingItem")) {
            NBTTagCompound tagTyingItem = tag.getCompoundTag("tyingItem");
            tyingItem = ItemStack.loadItemStackFromNBT(tagTyingItem);
        }

        long dryingStartTicks = tag.getLong("dryingStartTicks");
        boolean tyingItemUsedUp = tag.getBoolean("tyingItemUsedUp");

        return new DryingRackItem(dryingItem, tyingItem, dryingStartTicks, tyingItemUsedUp);
    }

}
