package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.api.Crafting.Builders.DryingRackFoodRecipeBuilder;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingFoodRecipe;
import net.minecraft.item.ItemStack;

public class DryingRackFoodRecipe extends DryingRackRecipe implements IDryingFoodRecipe {

    private final boolean allowSmoke;
    private final int smokeDuration;

    public DryingRackFoodRecipe(ItemStack inputItem, ItemStack outputItem, int duration, boolean requiresTyingEquipment, boolean allowSmoke, int smokeDuration) {
        super(inputItem, outputItem, null, duration, true, false, false, false, false, false, requiresTyingEquipment);

        this.allowSmoke = allowSmoke;
        this.smokeDuration = smokeDuration;
    }

    @Override
    public boolean isAllowSmoke() {
        return allowSmoke;
    }

    @Override
    public int getSmokeDuration() {
        return smokeDuration;
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
