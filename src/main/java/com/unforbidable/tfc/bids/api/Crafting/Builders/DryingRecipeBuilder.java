package com.unforbidable.tfc.bids.api.Crafting.Builders;

import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import net.minecraft.item.ItemStack;

public class DryingRecipeBuilder {

    protected ItemStack inputItem;
    protected ItemStack outputItem;
    protected ItemStack destroyedOutputItem;
    protected int duration;
    protected boolean requiresDry;
    protected boolean requiresWet;
    protected boolean requiresCover;
    protected boolean requiresWarm;
    protected boolean requiresFreezing;
    protected boolean requiresNotWet;

    public DryingRecipeBuilder consumes(ItemStack inputItem) {
        this.inputItem = inputItem;

        return this;
    }

    public DryingRecipeBuilder produces(ItemStack outputItem, ItemStack destroyedOutputItem) {
        this.outputItem = outputItem;
        this.destroyedOutputItem = destroyedOutputItem;

        return this;
    }

    public DryingRecipeBuilder produces(ItemStack outputItem) {
        return produces(outputItem, null);
    }

    public DryingRecipeBuilder hours(int duration) {
        this.duration = duration;

        return this;
    }

    public DryingRecipeBuilder dry() {
        this.requiresDry = true;

        return this;
    }

    public DryingRecipeBuilder wet() {
        this.requiresWet = true;

        return this;
    }

    public DryingRecipeBuilder warm() {
        this.requiresWarm = true;

        return this;
    }

    public DryingRecipeBuilder freezing() {
        this.requiresFreezing = true;

        return this;
    }

    public DryingRecipeBuilder cover() {
        this.requiresCover = true;

        return this;
    }

    public DryingRecipeBuilder notWet() {
        this.requiresNotWet = true;

        return this;
    }

    public DryingRecipe build() {
        return new DryingRecipe(inputItem, outputItem, destroyedOutputItem, duration, requiresDry, requiresWet, requiresCover, requiresWarm, requiresFreezing, requiresNotWet);
    }

}
