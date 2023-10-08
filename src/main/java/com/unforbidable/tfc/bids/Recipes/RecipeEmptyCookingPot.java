package com.unforbidable.tfc.bids.Recipes;

import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPot;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeEmptyCookingPot implements IRecipe {

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack original = null;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack is = inventory.getStackInSlot(i);
            if (is != null) {
                original = is;
            }
        }

        if (original != null && original.hasTagCompound()) {
            ItemStack empty = original.copy();

            TileEntityCookingPot dummy = new TileEntityCookingPot();
            dummy.readDataFromNBT(empty.getTagCompound());

            dummy.setLiquidEmptyNoUpdate();

            if (dummy.hasFluid() || dummy.hasInputItem() || dummy.hasLid() || dummy.hasAccessory()) {
                NBTTagCompound nbt = new NBTTagCompound();
                dummy.writeDataToNBT(nbt);
                empty.setTagCompound(nbt);
            } else {
                empty.setTagCompound(null);
            }

            return empty;
        }

        return null;
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(BidsBlocks.cookingPot, 1, 1);
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        int itemsFound = 0;
        ItemStack validItemStackFound = null;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack is = inventory.getStackInSlot(i);
            if (is != null) {
                itemsFound++;
                if (isValidCookingPotInputStack(is)) {
                    validItemStackFound = is;
                }
            }
        }

        // exactly one valid item needs to be found, and nothing else
        if (itemsFound == 1 && validItemStackFound != null) {
            return true;
        }

        return false;
    }

    protected boolean isValidCookingPotInputStack(ItemStack is) {
        return Block.getBlockFromItem(is.getItem()) == BidsBlocks.cookingPot && is.getItemDamage() > 0;
    }

}
