package com.unforbidable.tfc.bids.api.Crafting.Builders;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.api.Crafting.PrepIngredient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PrepIngredientBuilder {

    private final List<ItemStack> allowedItemStacks = new ArrayList<ItemStack>();
    private final List<String> allowedOreNames = new ArrayList<String>();
    private final List<EnumFoodGroup> allowedFoodGroups = new ArrayList<EnumFoodGroup>();

    private final List<ItemStack> deniedItemStacks = new ArrayList<ItemStack>();
    private final List<String> deniedOreNames = new ArrayList<String>();
    private final List<EnumFoodGroup> deniedFoodGroups = new ArrayList<EnumFoodGroup>();

    public PrepIngredientBuilder allow(ItemStack itemStack) {
        allowedItemStacks.add(itemStack);

        return this;
    }

    public PrepIngredientBuilder allow(Item item) {
        allowedItemStacks.add(new ItemStack(item));

        return this;
    }

    public PrepIngredientBuilder allow(Item item, int damage) {
        allowedItemStacks.add(new ItemStack(item, 1, damage));

        return this;
    }

    public PrepIngredientBuilder allow(String oreName) {
        allowedOreNames.add(oreName);

        return this;
    }

    public PrepIngredientBuilder allow(EnumFoodGroup foodGroup) {
        allowedFoodGroups.add(foodGroup);

        return this;
    }

    public PrepIngredientBuilder deny(ItemStack itemStack) {
        deniedItemStacks.add(itemStack);

        return this;
    }

    public PrepIngredientBuilder deny(Item item) {
        deniedItemStacks.add(new ItemStack(item));

        return this;
    }

    public PrepIngredientBuilder deny(Item item, int damage) {
        deniedItemStacks.add(new ItemStack(item, 1, damage));

        return this;
    }

    public PrepIngredientBuilder deny(String oreName) {
        deniedOreNames.add(oreName);

        return this;
    }

    public PrepIngredientBuilder deny(EnumFoodGroup foodGroup) {
        deniedFoodGroups.add(foodGroup);

        return this;
    }

    public PrepIngredient build() {
        return new PrepIngredient(allowedItemStacks, allowedOreNames, allowedFoodGroups, deniedItemStacks, deniedOreNames, deniedFoodGroups);
    }

}
