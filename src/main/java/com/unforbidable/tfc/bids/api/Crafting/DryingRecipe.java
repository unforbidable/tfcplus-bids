package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Interfaces.ISimpleRecipeMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DryingRecipe implements ISimpleRecipeMatcher<ItemStack> {

    private final ItemStack inputItem;
    private final ItemStack outputItem;
    private final ItemStack destroyedOutputItem;
    private final int duration;
    private final boolean requiresDry;
    private final boolean requiresWet;
    private final boolean requiresCover;
    private final boolean requiresWarm;
    private final boolean requiresFreezing;
    private final boolean requiresNotWet;

    public DryingRecipe(ItemStack inputItem, ItemStack outputItem, ItemStack destroyedOutputItem, int duration, boolean requiresDry, boolean requiresWet, boolean requiresCover, boolean requiresWarm, boolean requiresFreezing, boolean requiresNotWet) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.destroyedOutputItem = destroyedOutputItem;
        this.duration = duration;
        this.requiresDry = requiresDry;
        this.requiresWet = requiresWet;
        this.requiresCover = requiresCover;
        this.requiresWarm = requiresWarm;
        this.requiresFreezing = requiresFreezing;
        this.requiresNotWet = requiresNotWet;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isRequiresDry() {
        return requiresDry;
    }

    public boolean isRequiresWet() {
        return requiresWet;
    }

    public boolean isRequiresCover() {
        return requiresCover;
    }

    public boolean isRequiresWarm() {
        return requiresWarm;
    }

    public boolean isRequiresFreezing() {
        return requiresFreezing;
    }

    public boolean isRequiresNotWet() {
        return requiresNotWet;
    }

    @Override
    public boolean matches(ItemStack ingredient) {
        return matchesIngredient(ingredient);
    }

    protected boolean matchesIngredient(ItemStack ingredient) {
        return inputItem.getItem() == ingredient.getItem() && (inputItem.getItemDamage() == ingredient.getItemDamage()
            || inputItem.getItemDamage() == OreDictionary.WILDCARD_VALUE);
    }

    public ItemStack getResult(ItemStack ingredient) {
        return outputItem.copy();
    }

    public ItemStack getDestroyedResult(ItemStack ingredient) {
        return destroyedOutputItem != null ? destroyedOutputItem.copy() : null;
    }

}
