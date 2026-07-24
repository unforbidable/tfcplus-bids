package com.unforbidable.tfc.bids.api.features.cooking;

public class CookingCheeseRecipeBuilder extends CookingRecipeBuilder {

    private boolean allowInfusion;

    public CookingCheeseRecipeBuilder allowingInfusion() {
        this.allowInfusion = true;
        return this;
    }

    @Override
    public CookingCheeseRecipe build() {
        return new CookingCheeseRecipe(inputFluidStack, secondaryInputFluidStack,
            outputFluidStack, secondaryOutputFluidStack,
            inputItemStack, outputItemStack,
            accessory, lidUsage, minHeatLevel, maxHeatLevel, time, fixedTime,
            allowInfusion);
    }

}
