package com.unforbidable.tfc.bids.api.Interfaces;

import com.dunk.tfc.api.Enums.EnumFoodGroup;

import net.minecraftforge.fluids.Fluid;

public interface IDrinkable {

    Fluid getFluid();

    String getName();

    float getCalories();

    EnumFoodGroup getFoodGroup();

    float getWaterRestoreRatio();

    float getAlcoholContent();

    int getAlcoholTier();

}
