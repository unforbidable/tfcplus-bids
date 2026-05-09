package com.unforbidable.tfc.bids.core.drink.registry;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import net.minecraftforge.fluids.Fluid;

public class DrinkFluid {

    public final String name;
    public final Fluid fluid;
    public final float waterRestoreRatio;
    public final EnumFoodGroup foodGroup;
    public final float calories;
    public final int alcoholTier;
    public final float alcoholContent;

    public DrinkFluid(String name, Fluid fluid, float waterRestoreRatio) {
        this(name, fluid, waterRestoreRatio, 0, EnumFoodGroup.None, 0, 0);
    }

    public DrinkFluid(String name, Fluid fluid, float waterRestoreRatio, float calories) {
        this(name, fluid, waterRestoreRatio, calories, EnumFoodGroup.None, 0, 0);
    }

    public DrinkFluid(String name, Fluid fluid, float waterRestoreRatio, float calories, EnumFoodGroup foodGroup) {
        this(name, fluid, waterRestoreRatio, calories, foodGroup, 0, 0);
    }

    public DrinkFluid(String name, Fluid fluid, float waterRestoreRatio, float calories, int alcoholTier, float alcoholContent) {
        this(name, fluid, waterRestoreRatio, calories, EnumFoodGroup.None, alcoholTier, alcoholContent);
    }

    public DrinkFluid(String name, Fluid fluid, int alcoholTier, float alcoholContent) {
        this(name, fluid, 0, 0, EnumFoodGroup.None, alcoholTier, alcoholContent);
    }

    public DrinkFluid(String name, Fluid fluid, float waterRestoreRatio, float calories, EnumFoodGroup foodGroup, int alcoholTier, float alcoholContent) {
        this.name = name;
        this.fluid = fluid;
        this.waterRestoreRatio = waterRestoreRatio;
        this.foodGroup = foodGroup;
        this.calories = calories;
        this.alcoholTier = alcoholTier;
        this.alcoholContent = alcoholContent;
    }

}
