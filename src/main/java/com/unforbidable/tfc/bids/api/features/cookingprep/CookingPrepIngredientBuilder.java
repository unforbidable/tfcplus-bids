package com.unforbidable.tfc.bids.api.features.cookingprep;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CookingPrepIngredientBuilder {

    private final List<ItemStack> allowedItemStacks = new ArrayList<>();
    private final List<String> allowedOreNames = new ArrayList<>();
    private final List<EnumFoodGroup> allowedFoodGroups = new ArrayList<>();

    private final List<ItemStack> deniedItemStacks = new ArrayList<>();
    private final List<String> deniedOreNames = new ArrayList<>();
    private final List<EnumFoodGroup> deniedFoodGroups = new ArrayList<>();

    public CookingPrepIngredientBuilder allow(ItemStack itemStack) {
        allowedItemStacks.add(itemStack);

        return this;
    }

    public CookingPrepIngredientBuilder allow(Item item) {
        allowedItemStacks.add(new ItemStack(item));

        return this;
    }

    public CookingPrepIngredientBuilder allow(Item item, int damage) {
        allowedItemStacks.add(new ItemStack(item, 1, damage));

        return this;
    }

    public CookingPrepIngredientBuilder allow(String oreName) {
        allowedOreNames.add(oreName);

        return this;
    }

    public CookingPrepIngredientBuilder allow(EnumFoodGroup foodGroup) {
        allowedFoodGroups.add(foodGroup);

        return this;
    }

    public CookingPrepIngredientBuilder deny(ItemStack itemStack) {
        deniedItemStacks.add(itemStack);

        return this;
    }

    public CookingPrepIngredientBuilder deny(Item item) {
        deniedItemStacks.add(new ItemStack(item));

        return this;
    }

    public CookingPrepIngredientBuilder deny(Item item, int damage) {
        deniedItemStacks.add(new ItemStack(item, 1, damage));

        return this;
    }

    public CookingPrepIngredientBuilder deny(String oreName) {
        deniedOreNames.add(oreName);

        return this;
    }

    public CookingPrepIngredientBuilder deny(EnumFoodGroup foodGroup) {
        deniedFoodGroups.add(foodGroup);

        return this;
    }

    public CookingPrepIngredient build() {
        return new CookingPrepIngredient(allowedItemStacks, allowedOreNames, allowedFoodGroups, deniedItemStacks, deniedOreNames, deniedFoodGroups);
    }

}
