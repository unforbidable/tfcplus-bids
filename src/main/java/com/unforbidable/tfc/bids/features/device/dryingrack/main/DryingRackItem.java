package com.unforbidable.tfc.bids.features.device.dryingrack.main;

import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DryingRackItem extends DryingItem {

    public ItemStack tyingItem;
    public boolean tyingItemUsedUp;

    public ItemStack getCurrentTyingItem() {
        if (tyingItem != null) {
            if (!tyingItemUsedUp) {
                // return if not used up
                return tyingItem;
            }

            DryingRackTyingEquipment te = DryingRackHelper.findTyingEquipment(tyingItem);
            if (te != null && te.isReusable) {
                // unless if reusable return anyway
                return tyingItem;
            }
        }

        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (tyingItem != null) {
            NBTTagCompound tagItem = new NBTTagCompound();
            tyingItem.writeToNBT(tagItem);
            tag.setTag("tyingItem", tagItem);
        }

        tag.setBoolean("tyingItemUsedUp", tyingItemUsedUp);
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

        tyingItemUsedUp = tag.getBoolean("tyingItemUsedUp");
    }

    public static DryingRackItem loadDryingRackItemFromNBT(NBTTagCompound tag) {
        DryingRackItem dryingRackItem = new DryingRackItem();
        dryingRackItem.readFromNBT(tag);
        return dryingRackItem;
    }

}
