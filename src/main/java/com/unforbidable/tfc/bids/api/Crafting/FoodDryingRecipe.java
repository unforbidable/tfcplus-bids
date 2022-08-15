package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;

import net.minecraft.item.ItemStack;

public class FoodDryingRecipe extends DryingRecipe {

    public FoodDryingRecipe(ItemStack item, int duration, boolean requiresTyingEquipment) {
        super(item, item, duration, requiresTyingEquipment);
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return super.matches(itemStack)
                && !Food.isDried(itemStack) && !Food.isCooked(itemStack);
    }

    @Override
    public ItemStack getCraftingResult(ItemStack itemStack) {
        ItemStack output = itemStack.copy();
        Food.setDried(output, Food.DRYHOURS);
        return output;
    }

    @Override
    public float getInitialProgress(ItemStack itemStack) {
        // Some food stuffs are already partially dried
        // and this lets the drying rack know
        int dried = Food.getDried(itemStack);
        float progress = (float) dried / Food.DRYHOURS;

        Bids.LOG.debug("Food drying started with progress: " + progress + ", from hours: " + dried);

        return progress;
    }

    @Override
    public void onProgress(ItemStack itemStack, float progressTotal, float progressLastDelta) {
        super.onProgress(itemStack, progressTotal, progressLastDelta);

        // When food is drying the NBT is updated to track the progress
        // The progress saved is scaled to 4 hours
        // because above 4 hours the food is considered dried
        // So for example if the recipe drying time is set to 12 hours
        // the progress is saved every 12/4 = 3 hours
        int dried = (int) Math.floor(progressTotal * Food.DRYHOURS);
        Food.setDried(itemStack, dried);

        Bids.LOG.debug("Food drying progress: " + progressTotal + ", to hours rounded: " + dried);
    }

}
