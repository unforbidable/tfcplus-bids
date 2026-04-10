package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.api.Crafting.Builders.DryingRackFoodRecipeBuilder;
import net.minecraft.item.ItemStack;

public class DryingRackFoodRecipe extends DryingRackRecipe {

    public DryingRackFoodRecipe(ItemStack inputItem, ItemStack outputItem, int duration, boolean requiresTyingEquipment) {
        super(inputItem, outputItem, null, duration, true, false, false, false, false, false, requiresTyingEquipment);
    }

    public boolean matches(ItemStack itemStack) {
        return super.matches(itemStack) &&
            !Food.isCooked(itemStack);
    }

    public static DryingRackFoodRecipeBuilder builder() {
        return new DryingRackFoodRecipeBuilder();
    }

    @Override
    public ItemStack getResult(ItemStack itemStack) {
        ItemStack output = itemStack.copy();
        Food.setDried(output, Food.DRYHOURS);
        return output;
    }

}
