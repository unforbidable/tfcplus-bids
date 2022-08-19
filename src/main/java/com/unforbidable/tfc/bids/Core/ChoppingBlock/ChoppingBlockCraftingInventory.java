package com.unforbidable.tfc.bids.Core.ChoppingBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ChoppingBlockCraftingInventory implements IInventory {

    public static final int MAX_STORAGE = 2;

    ItemStack[] storage = new ItemStack[MAX_STORAGE];

    @Override
    public int getSizeInventory() {
        return MAX_STORAGE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return storage[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (storage[slot] != null) {
            if (storage[slot].stackSize <= amount) {
                ItemStack itemstack = storage[slot];
                storage[slot] = null;
                return itemstack;
            }
            ItemStack itemstack1 = storage[slot].splitStack(amount);
            if (storage[slot].stackSize == 0)
                storage[slot] = null;
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        storage[slot] = itemStack;
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return false;
    }

}
