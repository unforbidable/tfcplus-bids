package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.api.Interfaces.IDrinkable;

import net.minecraftforge.fluids.Fluid;

public class Drink implements IDrinkable {

    final Fluid fluid;
    final String name;
    float calories;
    EnumFoodGroup foodGroup;
    float waterRestoreRatio;
    float alcoholContent;
    int acloholTier;

    public Drink(Fluid fluid, String name) {
        this.fluid = fluid;
        this.name = name;
    }

    public Drink setWaterRestoreRatio(float waterRestoreRatio) {
        this.waterRestoreRatio = waterRestoreRatio;
        return this;
    }

    public Drink setCalories(float calories) {
        this.calories = calories;
        return this;
    }

    public Drink setFoodGroup(EnumFoodGroup foodGroup) {
        this.foodGroup = foodGroup;
        return this;
    }

    public Drink setAlcoholTier(int acloholTier) {
        this.acloholTier = acloholTier;
        return this;
    }

    public Drink setAlcoholContent(float alcoholContent) {
        this.alcoholContent = alcoholContent;
        return this;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getCalories() {
        return calories;
    }

    @Override
    public EnumFoodGroup getFoodGroup() {
        return foodGroup;
    }

    @Override
    public float getWaterRestoreRatio() {
        return waterRestoreRatio;
    }

    @Override
    public float getAlcoholContent() {
        return alcoholContent;
    }

    @Override
    public int getAlcoholTier() {
        return acloholTier;
    }

}
