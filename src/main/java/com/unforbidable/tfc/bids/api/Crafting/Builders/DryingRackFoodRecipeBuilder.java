package com.unforbidable.tfc.bids.api.Crafting.Builders;

import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.api.Crafting.DryingRackFoodRecipe;
import net.minecraft.item.ItemStack;

public class DryingRackFoodRecipeBuilder extends DryingRackRecipeBuilder {

    boolean allowSmoke;
    int smokeDuration;

    public DryingRackFoodRecipeBuilder smoke(int duration) {
        allowSmoke = true;
        smokeDuration = duration;

        return this;
    }

    @Override
    public DryingRackFoodRecipe build() {
        // For drying food, output item is automatically added
        ItemStack outputItem = this.inputItem.copy();
        Food.setDried(outputItem, Food.DRYHOURS);

        return new DryingRackFoodRecipe(inputItem, outputItem, duration, requiresTyingEquipment, allowSmoke, smokeDuration);
    }

}
