package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.api.Crafting.Builders.PrepIngredientBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class PrepIngredient {

    private final List<ItemStack> allowedItemStacks;
    private final List<String> allowedOreNames;
    private final List<EnumFoodGroup> allowedFoodGroups;

    private final List<ItemStack> deniedItemStacks;
    private final List<String> deniedOreNames;
    private final List<EnumFoodGroup> deniedFoodGroups;

    public PrepIngredient(List<ItemStack> allowedItemStacks, List<String> allowedOreNames, List<EnumFoodGroup> allowedFoodGroups, List<ItemStack> deniedItemStacks, List<String> deniedOreNames, List<EnumFoodGroup> deniedFoodGroups) {
        this.allowedItemStacks = allowedItemStacks;
        this.allowedOreNames = allowedOreNames;
        this.allowedFoodGroups = allowedFoodGroups;
        this.deniedItemStacks = deniedItemStacks;
        this.deniedOreNames = deniedOreNames;
        this.deniedFoodGroups = deniedFoodGroups;
    }

    public PrepIngredientSpec toSpec() {
        return new PrepIngredientSpec(this);
    }

    public PrepIngredientSpec toSpec(float weight) {
        return new PrepIngredientSpec(this, weight);
    }

    public PrepIngredientSpec toSpec(float weight, boolean required) {
        return new PrepIngredientSpec(this, weight, required);
    }

    public List<ItemStack> getAllowedItemStacks() {
        return allowedItemStacks;
    }

    public List<String> getAllowedOreNames() {
        return allowedOreNames;
    }

    public List<EnumFoodGroup> getAllowedFoodGroups() {
        return allowedFoodGroups;
    }

    public List<ItemStack> getDeniedItemStacks() {
        return deniedItemStacks;
    }

    public List<String> getDeniedOreNames() {
        return deniedOreNames;
    }

    public List<EnumFoodGroup> getDeniedFoodGroups() {
        return deniedFoodGroups;
    }

    public boolean matches(ItemStack ingredient) {
        if (allowedItemStacks.size() == 0 && allowedOreNames.size() == 0 && allowedFoodGroups.size() == 0) {
            // Implicit allow any usable food
            if (!isUsableFood(ingredient)) {
                return false;
            }
        } else {
            // Matches any allowed stack, ore or food group
            if (allowedItemStacks.size() > 0 && !matchesAnyFromItemStacks(ingredient, allowedItemStacks) ||
                allowedOreNames.size() > 0 && !matchesAnyFromOreNames(ingredient, allowedOreNames) ||
                allowedFoodGroups.size() > 0 && !matchesAnyFromFoodGroups(ingredient, allowedFoodGroups)) {
                return false;
            }
        }

        // And does not match any denied stack, ore or food group
        return (deniedItemStacks.size() == 0 || !matchesAnyFromItemStacks(ingredient, deniedItemStacks)) &&
            (deniedOreNames.size() == 0 || !matchesAnyFromOreNames(ingredient, deniedOreNames)) &&
            (deniedFoodGroups.size() == 0 || !matchesAnyFromFoodGroups(ingredient, deniedFoodGroups));
    }

    private boolean isUsableFood(ItemStack ingredient) {
        return ingredient.getItem() instanceof IFood &&
            ((IFood)ingredient.getItem()).isUsable(ingredient);
    }

    private boolean matchesAnyFromItemStacks(ItemStack ingredient, List<ItemStack> itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (matchesItemStack(ingredient, itemStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean matchesAnyFromOreNames(ItemStack ingredient, List<String> oreNames) {
        for (String oreName : oreNames) {
            if (matchesAnyFromItemStacks(ingredient, OreDictionary.getOres(oreName, false))) {
                return true;
            }
        }

        return false;
    }

    private boolean matchesAnyFromFoodGroups(ItemStack ingredient, List<EnumFoodGroup> foodGroups) {
        if (ingredient.getItem() instanceof IFood) {
            IFood food = (IFood) ingredient.getItem();
            for (EnumFoodGroup foodGroup : foodGroups) {
                if (food.getFoodGroup() == foodGroup) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchesItemStack(ItemStack ingredient, ItemStack allowed) {
        return ingredient.getItem() == allowed.getItem() &&
            (allowed.getItemDamage() == OreDictionary.WILDCARD_VALUE || allowed.getItemDamage() == ingredient.getItemDamage());
    }

    public static PrepIngredientBuilder builder() {
        return new PrepIngredientBuilder();
    }

    public static PrepIngredient from(Item item) {
        return builder()
            .allow(item)
            .build();
    }

    public static PrepIngredient from(ItemStack ingredient) {
        return builder()
            .allow(ingredient)
            .build();
    }

}
