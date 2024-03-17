package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class CookingMixture {

    private final String name;
    private final int color;
    private final boolean useDefaultName;
    private final ItemStack cookedMeal;

    public CookingMixture(String name, int color, boolean useDefaultName, ItemStack cookedMeal) {
        this.name = name;
        this.color = color;
        this.useDefaultName = useDefaultName;
        this.cookedMeal = cookedMeal;
    }

    public CookingMixture(String name, int color, ItemStack cookedMeal) {
        this(name, color, false, cookedMeal);
    }

    public CookingMixture(String name, int color, boolean useDefaultName) {
        this(name, color, useDefaultName, null);
    }

    public CookingMixture(String name, int color) {
        this(name, color, false,null);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public boolean isUseDefaultName() {
        return useDefaultName;
    }

    public ItemStack getCookedMeal() {
        return cookedMeal;
    }

    public boolean isReady() {
        return cookedMeal != null;
    }

}
