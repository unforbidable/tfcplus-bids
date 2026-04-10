package com.unforbidable.tfc.bids.Core.Drying;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Crafting.DryingRackFoodRecipe;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DryingHelper {

    public static ItemStack getResultItem(DryingItem dryingItem, DryingRecipe recipe) {
        ItemStack result = recipe.getResult(dryingItem.inputItem);
        if (result.getItem() instanceof ItemFoodTFC) {
            // Copy weight of food items
            Food.setWeight(result, Food.getWeight(dryingItem.inputItem));
        }
        return result;
    }

    public static ItemStack getDestroyedResultItem(DryingItem dryingItem, DryingRecipe recipe) {
        return recipe.getDestroyedResult(dryingItem.inputItem);
    }

    public static void initializeInputItemProgress(DryingItem dryingItem, DryingRecipe recipe) {
        if (recipe instanceof DryingRackFoodRecipe && dryingItem.inputItem.getItem() instanceof ItemFoodTFC) {
            // Some food stuffs are already partially dried
            // and this lets the drying engine know
            int dried = Food.getDried(dryingItem.inputItem);
            dryingItem.progress = (float) dried / Food.DRYHOURS;
        }
    }

    public static void initializeInputItemWetness(DryingItem dryingItem) {
        if (dryingItem.inputItem.getItem() instanceof ItemClothing) {
            // take wetness from clothes
            int wetness = TFC_Core.getClothingWetness(dryingItem.inputItem);
            dryingItem.wetness = Math.min(wetness / 4000f, 1);
        }
    }

    public static void applyInputItemProgress(DryingItem dryingItem, DryingRecipe recipe) {
        if (recipe instanceof DryingRackFoodRecipe && dryingItem.inputItem.getItem() instanceof ItemFoodTFC) {
            // When food is drying the NBT is updated to track progress
            // The progress saved is scaled to 4 hours
            // because above 4 hours the food is considered dried
            // So for example if the recipe drying time is set to 12 hours
            // the progress is saved every 12/4 = 3 hours
            int dried = (int) Math.floor(dryingItem.progress * Food.DRYHOURS);
            Food.setDried(dryingItem.inputItem, dried);
        }
    }

    public static void applyInputItemWetness(DryingItem dryingItem) {
        if (dryingItem.inputItem.getItem() instanceof ItemClothing) {
            if (!dryingItem.inputItem.hasTagCompound()) {
                dryingItem.inputItem.setTagCompound(new NBTTagCompound());
                dryingItem.inputItem.getTagCompound().setLong("lastWorn", TFC_Time.getTotalDays());
            }
            int wetness = Math.round(dryingItem.wetness * 4000);
            dryingItem.inputItem.getTagCompound().setInteger("wetness", wetness);
        }
    }

    public static void handleNormalDry(World world, int x, int y, int z, ItemStack itemStack, long ticks) {
        NBTTagCompound nbt = itemStack.stackTagCompound;
        if (nbt == null) {
            nbt = new NBTTagCompound();
            nbt.setLong("lastWorn", TFC_Time.getTotalDays());
            itemStack.stackTagCompound = nbt;
            return;
        }
        int wetness = nbt.getInteger("wetness");
        if (wetness > 0) {
            float humidity = TFC_Core.getHumidity(world, x, y, z);
            wetness -= ticks * (world.rand.nextFloat() > humidity ? 1 : 0);
            wetness = Math.max(0, wetness);
        }
        nbt.setInteger("wetness", wetness);
        itemStack.stackTagCompound = nbt;
    }

    public static void handleClothesInRain(ItemStack itemStack, int ticks) {
        NBTTagCompound nbt = itemStack.stackTagCompound;
        if (nbt == null) {
            nbt = new NBTTagCompound();
            nbt.setLong("lastWorn", TFC_Time.getTotalDays());
            itemStack.stackTagCompound = nbt;
            return;
        }
        int wetness = nbt.getInteger("wetness");
        if (wetness >= 0) {
            wetness += 2 * ticks;
            wetness = Math.min(2000, wetness);
        }
        nbt.setInteger("wetness", wetness);
        itemStack.stackTagCompound = nbt;
    }

    public static String getProgressInfoString(float progress) {
        int roundedProgress = (int)Math.floor(progress * 100);
        return roundedProgress > 0 ? String.format(" (%d%%)", roundedProgress) : "";
    }

    public static String getItemStackInfoString(ItemStack item) {
        return (item.stackSize > 1 ? (item.stackSize + "x") : "") + item.getDisplayName();
    }

    public static WetnessInfo getWetnessInfo(ItemStack inputItem) {
        if (inputItem.getItem() instanceof ItemClothing) {
            return new WetnessInfo(1000, 1);
        } else {
            return BidsRegistry.WETNESS.get(inputItem);
        }
    }

}
