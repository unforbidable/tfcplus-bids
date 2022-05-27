package com.unforbidable.tfc.bids.Recipes;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleLiquidItem;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleLiquidStorage;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeCrucibleConversion implements IRecipe {

    final boolean fromTFC;

    public RecipeCrucibleConversion(boolean fromTFC) {
        this.fromTFC = fromTFC;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack original = null;
        ItemStack converted = fromTFC ? getConvertedOutputFromTFC() : getConvertedOutputToTFC();

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack is = inventory.getStackInSlot(i);
            if (is != null) {
                original = is;
            }
        }

        if (original != null) {
            NBTTagCompound tag = original.getTagCompound();
            if (tag != null) {
                converted.setTagCompound(fromTFC ? convertNBTFromTFC(tag) : convertNBTToTFC(tag));

                Bids.LOG.debug("Converted NBT from: " + tag.toString() + " to: " + converted.getTagCompound());
            }
        }

        return converted;
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        int itemsFound = 0;
        Item validItem = fromTFC ? getValidInputItemFromTFC() : getValidInputItemToTFC();
        ItemStack validItemStackFound = null;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack is = inventory.getStackInSlot(i);
            if (is != null) {
                itemsFound++;
                if (is.getItem() == validItem) {
                    validItemStackFound = is;
                }
            }
        }

        // exactly one valid item needs to be found, and nothing else
        if (itemsFound == 1 && validItemStackFound != null) {
            if (fromTFC)
                return onMatchFromTFC(validItemStackFound);
            else
                return true;
        }

        return false;
    }

    public ItemStack getRecipeOutput() {
        return fromTFC ? getConvertedOutputFromTFC() : getConvertedOutputToTFC();
    }

    ItemStack getConvertedOutputFromTFC() {
        return new ItemStack(BidsBlocks.fireClayCrucible);
    }

    Item getValidInputItemFromTFC() {
        return Item.getItemFromBlock(TFCBlocks.crucible);
    }

    NBTTagCompound convertNBTFromTFC(NBTTagCompound origTag) {
        NBTTagCompound convertedTag = new NBTTagCompound();

        int temp = origTag.getInteger("temp");

        NBTTagList nbttaglist = origTag.getTagList("Metals", 10);
        if (nbttaglist.tagCount() > 0) {
            CrucibleLiquidStorage liquid = new CrucibleLiquidStorage();
            for (int i = 0; i < nbttaglist.tagCount(); i++) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                int id = nbttagcompound1.getInteger("ID");
                float amount = nbttagcompound1.getShort("Amount");
                // Added so that hopefully old worlds that stored metal as shorts
                // wont break
                float amountF = amount + nbttagcompound1.getFloat("AmountF");

                liquid.addLiquid(CrucibleHelper.getMetalFromItem(Item.getItemById(id)), amountF);
            }

            liquid.writeToNBT(convertedTag);
        } else {
            // When there is no liquid, we don't care about the temp
            temp = 0;
        }

        convertedTag.setInteger("liquidTemp", temp);

        return convertedTag;
    }

    boolean onMatchFromTFC(ItemStack matchingItemStack) {
        // When converting from TFC, make sure there are no items inside
        // otherwise those would be lost
        // so we reject the recipe
        NBTTagCompound tag = matchingItemStack.getTagCompound();
        if (tag != null) {
            NBTTagList nbttaglist = tag.getTagList("Items", 10);
            for (int i = 0; i < nbttaglist.tagCount(); i++) {
                NBTTagCompound itemsTag = nbttaglist.getCompoundTagAt(i);
                byte slot = itemsTag.getByte("Slot");
                if (slot >= 0 && slot < 2) {
                    ItemStack is = ItemStack.loadItemStackFromNBT(itemsTag);
                    if (is != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ItemStack getConvertedOutputToTFC() {
        return new ItemStack(TFCBlocks.crucible);
    }

    protected Item getValidInputItemToTFC() {
        return Item.getItemFromBlock(BidsBlocks.fireClayCrucible);
    }

    protected NBTTagCompound convertNBTToTFC(NBTTagCompound origTag) {
        NBTTagCompound convertedTag = new NBTTagCompound();

        int temp = origTag.getInteger("liquidTemp");
        convertedTag.setInteger("temp", temp);

        CrucibleLiquidStorage liquid = new CrucibleLiquidStorage();
        liquid.readFromNBT(origTag);
        if (liquid.getItemCount() > 0) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < liquid.getItemCount(); i++) {
                CrucibleLiquidItem it = liquid.getItem(i);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("ID", Item.getIdFromItem(it.getMetal().ingot));
                tag.setFloat("AmountF", it.getVolume());
                nbttaglist.appendTag(tag);
            }
            convertedTag.setTag("Metals", nbttaglist);
        }

        return convertedTag;
    }

}
