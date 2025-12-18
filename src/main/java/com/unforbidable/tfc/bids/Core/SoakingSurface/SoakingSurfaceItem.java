package com.unforbidable.tfc.bids.Core.SoakingSurface;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SoakingSurfaceItem {

    public final ItemStack soakingItem;
    public final long soakingStartTicks;

    public SoakingSurfaceItem(ItemStack soakingItem, long soakingStartTicks) {
        this.soakingItem = soakingItem;
        this.soakingStartTicks = soakingStartTicks;
    }

    public static SoakingSurfaceItem loadItemStackFromNBT(NBTTagCompound tag) {
        ItemStack soakingItem = null;

        if (tag.hasKey("soakingItem")) {
            NBTTagCompound tagSoakingItem = tag.getCompoundTag("soakingItem");
            soakingItem = ItemStack.loadItemStackFromNBT(tagSoakingItem);
        }

        long soakingStartTicks = tag.getLong("soakingStartTicks");

        return new SoakingSurfaceItem(soakingItem, soakingStartTicks);
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (soakingItem != null) {
            NBTTagCompound tagSoakingItem = new NBTTagCompound();
            soakingItem.writeToNBT(tagSoakingItem);
            tag.setTag("soakingItem", tagSoakingItem);
        }

        tag.setLong("soakingStartTicks", soakingStartTicks);
    }

}
