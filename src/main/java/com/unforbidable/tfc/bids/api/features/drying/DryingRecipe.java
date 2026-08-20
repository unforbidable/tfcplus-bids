package com.unforbidable.tfc.bids.api.features.drying;

import com.unforbidable.tfc.bids.api.util.SimpleRecipeMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DryingRecipe implements SimpleRecipeMatcher<ItemStack> {

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
    private final boolean requiresSmoke;
    private final int canSmokeDuration;

    public DryingRecipe(ItemStack inputItem, ItemStack outputItem, ItemStack destroyedOutputItem, int duration, boolean requiresDry, boolean requiresWet, boolean requiresCover, boolean requiresWarm, boolean requiresFreezing, boolean requiresNotWet, boolean requiresSmoke, int canSmokeDuration) {
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
        this.requiresSmoke = requiresSmoke;
        this.canSmokeDuration = canSmokeDuration;
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

    public boolean isRequiresSmoke() {
        return requiresSmoke;
    }

    public boolean canSmoke() {
        return getCanSmokeDuration() > 0;
    }

    public int getCanSmokeDuration() {
        return canSmokeDuration;
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
        if (outputItem != null) {
            return outputItem.copy();
        } else {
            // For drying/smoking Foodstuffs there is no output item
            return ingredient.copy();
        }
    }

    public ItemStack getDestroyedResult(ItemStack ingredient) {
        return destroyedOutputItem != null ? destroyedOutputItem.copy() : null;
    }

    public boolean hasResult() {
        return outputItem != null;
    }

}
