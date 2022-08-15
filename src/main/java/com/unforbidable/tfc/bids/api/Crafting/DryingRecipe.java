package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class DryingRecipe {

    final ItemStack output;
    final ItemStack input;
    final int duration;
    final boolean requiresTyingEquipment;

    public DryingRecipe(ItemStack output, ItemStack input, int duration, boolean requiresTyingEquipment) {
        this.output = output;
        this.input = input;
        this.duration = duration;
        this.requiresTyingEquipment = requiresTyingEquipment;
    }

    public boolean matches(ItemStack itemStack) {
        return itemStack.getItem() == input.getItem()
                && itemStack.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getCraftingResult(ItemStack itemStack) {
        return output;
    }

    public int getDuration() {
        return duration;
    }

    public boolean getRequiresTyingEquipment() {
        return requiresTyingEquipment;
    }

    public float getInitialProgress(ItemStack itemStack) {
        return 0f;
    }

    public void onProgress(ItemStack itemStack, float progressTotal, float progressLastDelta) {
    }

}
